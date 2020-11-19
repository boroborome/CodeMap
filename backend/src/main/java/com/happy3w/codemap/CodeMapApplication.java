package com.happy3w.codemap;

import com.happy3w.persistence.es.EsAssistant;
import com.happy3w.persistence.es.model.EsConnectConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.net.UnknownHostException;

@SpringBootApplication
public class CodeMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeMapApplication.class, args);
    }

    @ConfigurationProperties(prefix = "code-map.es")
    @Bean
    public EsConnectConfig esConnectionConfig() {
        return new EsConnectConfig();
    }

    @Bean
    public EsAssistant esAssistant(EsConnectConfig esConnectionConfig) throws UnknownHostException {
        return EsAssistant.from(esConnectionConfig);
    }
}
