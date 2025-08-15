package ru.pleshkova.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ru.pleshkova.infrastructure.configuration.properties.RestProperties;

@Configuration
@RequiredArgsConstructor
public class RestConfiguration {

    private final RestProperties restProperties;


    @Bean
    RestTemplate restTemplate() {
        final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(restProperties.getConnectTimeout());
        requestFactory.setReadTimeout(restProperties.getReadTimeout());
        return new RestTemplate(requestFactory);
    }

}
