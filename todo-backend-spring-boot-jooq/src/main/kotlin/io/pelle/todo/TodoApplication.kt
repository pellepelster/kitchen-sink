package io.pelle.todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication(exclude = [ContextInstanceDataAutoConfiguration::class])
@EnableScheduling
class TodoApplication {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /*
    val config = object : CloudWatchConfig {
        private val configuration = mapOf(
            "cloudwatch.namespace" to "todo-application",
            "cloudwatch.step" to Duration.ofMinutes(1).toString()
        )

        override fun get(key: String): String? =
            configuration[key]
    }

    @Bean
    fun cloudWatchMeterRegistry(): CloudWatchMeterRegistry {
        return CloudWatchMeterRegistry(
            config,
            Clock.SYSTEM,
            CloudWatchAsyncClient.create()
        )
    }
*/
}

fun main(args: Array<String>) {
    runApplication<TodoApplication>(*args)
}
