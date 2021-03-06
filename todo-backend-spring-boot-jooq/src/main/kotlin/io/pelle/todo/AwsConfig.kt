package io.pelle.todo

import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("prod")
@EnableContextRegion(useDefaultAwsRegionChain = true, autoDetect = true)
class AwsConfig
