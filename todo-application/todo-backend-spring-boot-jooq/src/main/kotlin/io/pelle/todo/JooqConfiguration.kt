package io.pelle.todo

import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
class JooqConfiguration(val dataSource: DataSource) {

    @Bean
    fun connectionProvider(): DataSourceConnectionProvider? {
        return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))
    }

    @Bean
    fun dsl(): DefaultDSLContext? {
        return DefaultDSLContext(configuration())
    }

    fun configuration(): DefaultConfiguration? {
        val jooqConfiguration = DefaultConfiguration()
        jooqConfiguration.set(connectionProvider())
        return jooqConfiguration
    }
}
