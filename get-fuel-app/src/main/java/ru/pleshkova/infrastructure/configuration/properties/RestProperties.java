package ru.pleshkova.infrastructure.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("rest")
public class RestProperties {

    private int connectTimeout;
    private int readTimeout;
    private String url;

}
