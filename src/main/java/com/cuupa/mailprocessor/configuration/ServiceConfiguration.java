package com.cuupa.mailprocessor.configuration;

import com.cuupa.mailprocessor.services.semantic.ExternSemanticService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServiceConfiguration {

    @Value("${mailprocessor.semanticUIrl")
    private String semanticUrl;

    @Bean
    public ExternSemanticService externSemanticService() {
        return new ExternSemanticService(restTemplate(), semanticUrl);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
