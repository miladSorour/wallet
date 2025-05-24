package org.milad.wallet.domain.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");

        Collection<SimpleGrantedAuthority> authorities = roles != null
                ? roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
                : List.of();

        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }
}