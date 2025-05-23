package org.milad.wallet.domain.security;

import lombok.RequiredArgsConstructor;
import org.milad.wallet.domain.user.User;
import org.milad.wallet.domain.user.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {

        if (event.getAuthentication() instanceof UsernamePasswordAuthenticationToken authentication) {
            CustomUserDetail userDetail = ((CustomUserDetail) authentication.getPrincipal());
            User user = userService.findByUsername(userDetail.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Invalid Username/Password"));
            userService.resetFailedAttempts(user);
        }

    }
}
