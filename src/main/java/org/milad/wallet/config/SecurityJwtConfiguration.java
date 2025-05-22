package org.milad.wallet.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.extern.slf4j.Slf4j;
import org.milad.wallet.domain.security.SecurityMetersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@Slf4j
public class SecurityJwtConfiguration {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Autowired
    WalletAppProperties properties;

    @Bean
    public JwtDecoder jwtDecoder(SecurityMetersService metersService) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                if (e.getMessage().contains("Invalid signature")) {
                    metersService.trackTokenInvalidSignature();
                } else if (e.getMessage().contains("Jwt expired at")) {
                    metersService.trackTokenExpired();
                } else if (
                        e.getMessage().contains("Invalid JWT serialization") ||
                                e.getMessage().contains("Malformed token") ||
                                e.getMessage().contains("Invalid unsecured/JWS/JWE")
                ) {
                    metersService.trackTokenMalformed();
                } else {
                    log.error("Unknown JWT error {}", e.getMessage());
                }
                throw e;
            }
        };
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = properties.getSecurity().getKey().getBytes();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
}