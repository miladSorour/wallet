package org.milad.wallet.common;

import lombok.RequiredArgsConstructor;
import org.milad.wallet.config.WalletAppProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

import static org.milad.wallet.config.SecurityJwtConfiguration.JWT_ALGORITHM;

@Component
@RequiredArgsConstructor
public class UtilJwt {

    private final JwtEncoder jwtEncoder;
    private final WalletAppProperties properties;

    @Value("${spring.application.name}")
    private String applicationName;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = Duration.ofMinutes(properties.getSecurity().getJwtExpMin()).toSeconds();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(applicationName)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

}
