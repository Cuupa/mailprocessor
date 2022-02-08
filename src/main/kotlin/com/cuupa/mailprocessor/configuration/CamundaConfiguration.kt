package com.cuupa.mailprocessor.configuration

import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration
import org.postgresql.Driver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@Configuration
@Profile("test", "local")
open class CamundaConfiguration {

    //@Bean
    open fun dataSource(): DataSource {
        return SimpleDriverDataSource().apply {
            setDriverClass(Driver::class.java)
            url = "jdbc:postgresql:10.80.22.5:5433/mailprocessor"
            username = "mailprocessor-postgresql"
            password = "Q!ZGRfn8"
        }
    }

   // @Bean
    open fun transactionManager(): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource())
    }

    //@Bean
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