package com.techchallenge.payments.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .additionalInterceptors(loggingInterceptor()) // Add interceptors
                .build();
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            // Logging or modifying headers
            System.out.println("Request URI: " + request.getURI());
            System.out.println("Request Method: " + request.getMethod());
            System.out.println("Request Headers: " + request.getHeaders());

            // Proceed with the request
            return execution.execute(request, body);
        };
    }
}

