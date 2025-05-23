package org.milad.wallet.config;


@SuppressWarnings("java:S2386")
public interface WalletDefaults {

    interface Security {
        String contentSecurityPolicy = "default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:";
        String key = "my-very-secret-key-which-should-be-long-and-secure";
        Long jwtExpMin = 60L;
    }
}
