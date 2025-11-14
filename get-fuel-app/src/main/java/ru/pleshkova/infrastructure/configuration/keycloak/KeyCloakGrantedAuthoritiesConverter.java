package ru.pleshkova.infrastructure.configuration.keycloak;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

@RequiredArgsConstructor
public class KeyCloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final GrantedAuthoritiesMapper grantedAuthoritiesMapper = createGrantedAuthoritiesMapper();
    private final String resourceName;

    private static GrantedAuthoritiesMapper createGrantedAuthoritiesMapper() {
        SimpleAuthorityMapper grantedAuthoritiesMapper = new SimpleAuthorityMapper();
        grantedAuthoritiesMapper.setConvertToUpperCase(true);
        return grantedAuthoritiesMapper;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> resourceAccess = source.getClaim("resource_access");
        if (resourceAccess != null) {
            Map<String, Object> access = (Map<String, Object>) resourceAccess.get(this.resourceName);
            if (access != null) {
                Collection<String> roles = (Collection<String>) access.get("roles");
                if (roles != null) {
                    List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(roles);
                    return (Collection<GrantedAuthority>) this.grantedAuthoritiesMapper.mapAuthorities(authorityList);
                }
            }
        }
        return Collections.emptyList();
    }
}
