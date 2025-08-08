package ru.pleshkova.infrastructure.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("job")
public class JobProperties {

    private Parameters someJob;

    @Data
    public static class Parameters {
        private boolean enabled;
        private String cron;
    }
}
