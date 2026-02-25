package ru.pleshkova.infrastructure.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeyCloakConfigProperties {
    private String url;
    private String realm;
    private String clientId;
    private String clientSecret;
    private Parameters users;
    private Timeout timeout;

    @Data
    public static class Parameters {
        private String clientId;
        private String clientSecret;
    }

    @Data
    public static class Timeout {
        private int read;
        private int connect;
    }
}
