package io.pelle.todo

import io.micrometer.cloudwatch2.CloudWatchConfig
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry
import io.micrometer.core.instrument.Clock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.aws.autoconfigure.context.ContextCredentialsAutoConfiguration
import org.springframework.cloud.aws.autoconfigure.metrics.CloudWatchProperties
import org.springframework.cloud.aws.context.annotation.ConditionalOnMissingAmazonClient
import org.springframework.cloud.aws.core.region.RegionProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import software.amazon.awssdk.auth.credentials.ContainerCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient

@Configuration(proxyBeanMethods = false)
@Import(
    ContextCredentialsAutoConfiguration::class
)
@AutoConfigureBefore(CompositeMeterRegistryAutoConfiguration::class, SimpleMetricsExportAutoConfiguration::class)
@AutoConfigureAfter(
    MetricsAutoConfiguration::class
)
@EnableConfigurationProperties(CloudWatchProperties::class)
@ConditionalOnProperty(prefix = "management.metrics.export.cloudwatch", name = ["namespace"])
@ConditionalOnClass(
    CloudWatchMeterRegistry::class, RegionProvider::class
)
class CloudWatchExport2AutoConfiguration {
    @Autowired(required = false)
    private val regionProvider: RegionProvider? = null
    @Bean
    @ConditionalOnProperty(value = ["management.metrics.export.cloudwatch.enabled"], matchIfMissing = true)
    fun cloudWatchMeterRegistry(
        config: CloudWatchConfig?,
        clock: Clock?,
        client: CloudWatchAsyncClient?
    ): CloudWatchMeterRegistry {
        return CloudWatchMeterRegistry(config, clock, client)
    }

    @Bean
    @ConditionalOnMissingAmazonClient(CloudWatchAsyncClient::class)
    fun amazonCloudWatchAsync(): CloudWatchAsyncClient? {
        return CloudWatchAsyncClient.builder().credentialsProvider(ContainerCredentialsProvider.builder().build()).region(Region.of(regionProvider?.region?.name)).build()
    }

    @Bean
    @ConditionalOnMissingBean
    fun cloudWatchConfig(cloudWatchProperties: CloudWatchProperties?): CloudWatchConfig {

        return object : CloudWatchConfig {
            private val configuration = mapOf(
                "cloudwatch.namespace" to cloudWatchProperties?.namespace,
                "cloudwatch.step" to cloudWatchProperties?.step
            )

            override fun get(key: String): String? {
                return configuration[key]?.toString()
            }
        }
    }

    @Bean
    @ConditionalOnMissingBean
    fun micrometerClock(): Clock {
        return Clock.SYSTEM
    }
}
