package org.milad.wallet.domain.security;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class SecurityMetersService {

    public static final String INVALID_TOKENS_METER_NAME = "security.authentication.invalid-tokens";
    public static final String INVALID_TOKENS_METER_DESCRIPTION =
            "Indicates validation error count of the tokens presented by the clients.";
    public static final String INVALID_TOKENS_METER_BASE_UNIT = "errors";
    public static final String INVALID_TOKENS_METER_CAUSE_DIMENSION = "cause";

    private final Counter tokenInvalidSignatureCounter;
    private final Counter tokenExpiredCounter;
    private final Counter tokenUnsupportedCounter;
    private final Counter tokenMalformedCounter;
    private final Counter incorrectLogin;

    public SecurityMetersService(MeterRegistry registry) {
        this.tokenInvalidSignatureCounter = invalidTokensCounterForCauseBuilder("invalid-signature").register(registry);
        this.tokenExpiredCounter = invalidTokensCounterForCauseBuilder("expired"). register(registry);
        this.tokenUnsupportedCounter = invalidTokensCounterForCauseBuilder("unsupported").register(registry);
        this.tokenMalformedCounter = invalidTokensCounterForCauseBuilder("malformed").register(registry);
        this.incorrectLogin = invalidTokensCounterForCauseBuilder("incorrect-login").register(registry);
    }

    private Counter.Builder invalidTokensCounterForCauseBuilder(String cause) {
        return Counter.builder(INVALID_TOKENS_METER_NAME)
                .baseUnit(INVALID_TOKENS_METER_BASE_UNIT)
                .description(INVALID_TOKENS_METER_DESCRIPTION)
                .tag(INVALID_TOKENS_METER_CAUSE_DIMENSION, cause);
    }

    public void trackTokenInvalidSignature() {
        this.tokenInvalidSignatureCounter.increment();
    }

    public void trackTokenExpired() {
        this.tokenExpiredCounter.increment();
    }

    public void trackTokenUnsupported() {
        this.tokenUnsupportedCounter.increment();
    }

    public void trackIncorrectLogin() {
        this.incorrectLogin.increment();
    }

    public void trackTokenMalformed() {
        this.tokenMalformedCounter.increment();
    }
}