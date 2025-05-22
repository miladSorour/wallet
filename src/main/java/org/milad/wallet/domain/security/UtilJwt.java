package org.milad.wallet.domain.security;

import com.nimbusds.jose.JWSAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
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

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = Duration.ofMinutes(60L).toSeconds();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("wallet")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .build();


        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }


}
