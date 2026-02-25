package ru.pleshkova.infrastructure.grpc;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.client.RestTemplate;
import ru.pleshkova.infrastructure.configuration.keycloak.KeyCloakGrantedAuthoritiesConverter;
import ru.pleshkova.infrastructure.configuration.properties.KeyCloakConfigProperties;

import java.net.URI;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(proxyTargetClass = true)
@ConditionalOnProperty(value = "grpc.authorization.enabled", matchIfMissing = true)
public class AuthorizationConfiguration {

    private final KeyCloakGrantedAuthoritiesConverter keyCloakGrantedAuthoritiesConverter;
    private final KeyCloakConfigProperties keyCloakConfigProperties;

    @Bean
    protected AuthenticationManager authenticationManager(JwtAuthenticationConverter jwtAuthenticationConverter) {
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider()));
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(keyCloakGrantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider() {
        final JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder());
        provider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
        return provider;
    }

    @Bean
    GrpcAuthenticationReader authenticationReader() {
        return new BearerAuthenticationReader(BearerTokenAuthenticationToken::new);
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    JwtDecoder jwtDecoder() {
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(keyCloakConfigProperties.getTimeout().getConnect());
        factory.setConnectionRequestTimeout(keyCloakConfigProperties.getTimeout().getRead());
        final RestTemplate restTemplate = new RestTemplate(factory);
        String uri = URI.create("%s/realms/%s/protocol/openid-connect/certs"
                        .formatted(keyCloakConfigProperties.getUrl(), keyCloakConfigProperties.getRealm()))
                .normalize().toString();
        final NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
                .withJwkSetUri(uri)
                .restOperations(restTemplate)
                .build();

        jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(uri));
        return jwtDecoder;
    }
}
