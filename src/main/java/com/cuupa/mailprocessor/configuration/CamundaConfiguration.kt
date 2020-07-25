package com.cuupa.mailprocessor.configuration

import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration
import org.h2.Driver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@Configuration
open class CamundaConfiguration {
    @Bean
    open fun dataSource(): DataSource {
        val dataSource = SimpleDriverDataSource()
        dataSource.setDriverClass(Driver::class.java)
        dataSource.url = "jdbc:h2:mem:camunda;DB_CLOSE_DELAY=-1"
        dataSource.username = "sa"
        dataSource.password = ""
        return dataSource
    }

    @Bean
    open fun transactionManager(): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource())
    }

    @Bean
    open fun processEngineConfiguration(): SpringProcessEngineConfiguration {
        val config = SpringProcessEngineConfiguration()
        config.processEngineName = "mailprocessor"
        config.dataSource = dataSource()
        config.transactionManager = transactionManager()
        config.databaseSchemaUpdate = "true"
        config.history = "full"
        config.isJobExecutorActivate = true
        return config
    }

    @Bean
    open fun processEngine(): ProcessEngineFactoryBean {
        val factoryBean = ProcessEngineFactoryBean()
        factoryBean.processEngineConfiguration = processEngineConfiguration()
        return factoryBean
    }
}