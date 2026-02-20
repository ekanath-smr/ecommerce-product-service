package com.example.ProductService.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {
    // create a bean for rest template and store it in the application context
    @Bean
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }
}
