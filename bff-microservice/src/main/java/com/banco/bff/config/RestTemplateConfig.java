package com.banco.bff.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        log.info("Configurando RestTemplate");

        return builder
                .requestFactory(this::clientHttpRequestFactory)
                .build();
    }

    /**
     * Configurar factory con timeouts
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        // Timeouts en milisegundos
        factory.setConnectTimeout(10000);     // 10 segundos
        factory.setReadTimeout(30000);        // 30 segundos

        return new BufferingClientHttpRequestFactory(factory);
    }
}