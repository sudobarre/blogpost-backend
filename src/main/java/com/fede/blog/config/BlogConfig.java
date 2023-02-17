package com.fede.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlogConfig {
    @Value("${spring.mail.username}")
    private String sender;

    @Bean
    public String sender() {
        return sender;
    }
}
