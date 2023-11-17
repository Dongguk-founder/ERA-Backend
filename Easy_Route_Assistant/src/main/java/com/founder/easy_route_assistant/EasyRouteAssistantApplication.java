package com.founder.easy_route_assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class EasyRouteAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyRouteAssistantApplication.class, args);
    }

}
