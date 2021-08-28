package io.pelle.todo

import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver

@Configuration
class WebConfiguration : WebMvcConfigurer {

    // snippet:static_files_config
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/todo-frontend-angular/")
            .resourceChain(true)
            .addResolver(object : PathResourceResolver() {
                override fun getResource(resourcePath: String, location: Resource): Resource? {
                    val requestedResource: Resource = location.createRelative(resourcePath)

                    return if (requestedResource.exists() && requestedResource.isReadable) requestedResource else ClassPathResource(
                        "/todo-frontend-angular/index.html"
                    )
                }
            })

    }
    // /snippet:static_files_config
}