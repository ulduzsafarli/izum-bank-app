package com.example.mybankapplication.model.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mail")
@Data
public class EmailProperties {
    private String from;
    private String subject;
    private String to;
}