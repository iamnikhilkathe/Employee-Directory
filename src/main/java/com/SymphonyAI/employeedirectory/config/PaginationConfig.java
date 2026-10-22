package com.SymphonyAI.employeedirectory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PaginationConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer(CustomPaginationProperties props) {
        return resolver -> {
            resolver.setFallbackPageable(PageRequest.of(0, props.getDefaultPageSize()));
            resolver.setMaxPageSize(props.getMaxPageSize());
        };
    }
}