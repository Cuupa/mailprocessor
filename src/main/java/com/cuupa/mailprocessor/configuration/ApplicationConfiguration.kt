package com.cuupa.mailprocessor.configuration;

import com.cuupa.mailprocessor.MailProcessorCamundaProcessApplication;
import com.cuupa.mailprocessor.MailprocessorConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync
@Import({DelegateConfiguration.class, ServiceConfiguration.class, CamundaConfiguration.class})
public class ApplicationConfiguration {

    @Value("$mailprocessor.config")
    private String configpath;

    @Autowired
    private RuntimeService runtimeService;

    @Bean
    public MailProcessorCamundaProcessApplication mailProcessorProcessApplication() {
        return new MailProcessorCamundaProcessApplication();
    }

    @Bean
    public MailprocessorConfiguration configuration(){
        return new MailprocessorConfiguration(configpath);
    }
}
