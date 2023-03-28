package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.meta")
public class Oauth2MetaProperties {
    private AppleProperties apple;

    @Getter @Setter
    public static class AppleProperties {
        private String keyId;
        private String teamId;
        private String issuer;
    }
}
