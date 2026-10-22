package com.SymphonyAI.employeedirectory.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "pagination")
public class CustomPaginationProperties {
    private int defaultPageSize = 20; // Fallback default value
    private int maxPageSize = 2000;    // Fallback default value


}
