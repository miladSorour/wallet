package org.milad.wallet.config;

import lombok.extern.log4j.Log4j2;
import org.milad.wallet.domain.security.CustomUserDetail;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Log4j2
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Long subject = 0L;

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AbstractAuthenticationToken authToken) {
            Object principal = authToken.getPrincipal();
            if (principal instanceof CustomUserDetail userDetail) {
                subject = userDetail.getId();
            }
        }

        log.debug("AuditorAwareImpl USER is : {}", subject);
        return Optional.of(subject);
    }
}
