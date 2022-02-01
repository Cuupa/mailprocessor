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
        return SimpleDriverDataSource().apply {
            setDriverClass(Driver::class.java)
            url = "jdbc:h2:mem:camunda;DB_CLOSE_DELAY=-1"
            username = "sa"
            password = ""
        }
    }

    @Bean
    open fun transactionManager(): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource())
    }

    @Bean
    open fun processEngineConfiguration(): SpringProcessEngineConfiguration {
        return SpringProcessEngineConfiguration().apply {
            processEngineName = "mailprocessor"
            dataSource = dataSource()
            transactionManager = transactionManager()
            databaseSchemaUpdate = "true"
            history = "full"
            isJobExecutorActivate = true
        }
    }

    /*@Bean
    open fun processEngine(): ProcessEngineFactoryBean {
        val factoryBean = ProcessEngineFactoryBean()
        factoryBean.processEngineConfiguration = processEngineConfiguration()
        return factoryBean
    }*/
}