package com.project.donuts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class DonutsApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(DonutsApplication.class, args);
    }
}
