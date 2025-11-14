package ru.pleshkova.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import ru.pleshkova.infrastructure.configuration.keycloak.KeyCloakGrantedAuthoritiesConverter;
import ru.pleshkova.infrastructure.configuration.properties.KeyCloakConfigProperties;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final KeyCloakConfigProperties keyCloakConfigProperties;

    @Bean
    @ConditionalOnProperty(prefix = "auth", name = "enable", havingValue = "false")
    SecurityFilterChain unprotectedFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/v1/api"))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return httpSecurity.build();

//        return httpSecurity.authorizeHttpRequests(authHttRequests -> authHttRequests.anyRequest().permitAll())
//                .build();
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authReq -> authReq
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated());

        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(usersKeyCloakJwtAuthConverter())));
        http.sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter usersKeyCloakJwtAuthConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(usersKeycloakGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    KeyCloakGrantedAuthoritiesConverter usersKeycloakGrantedAuthoritiesConverter() {
        return new KeyCloakGrantedAuthoritiesConverter(keyCloakConfigProperties.getUsers().getClientId());
    }



}
