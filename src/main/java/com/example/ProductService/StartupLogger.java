package com.example.ProductService;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger {

    @Value("${eureka.instance.hostname:NOT_FOUND}")
    private String hostname;

    @PostConstruct
    public void init() {
        System.out.println("Hostname = " + hostname);
    }
}
