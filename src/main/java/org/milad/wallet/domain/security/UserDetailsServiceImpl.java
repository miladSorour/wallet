package org.milad.wallet.domain.security;

import lombok.RequiredArgsConstructor;
import org.milad.wallet.domain.authority.Authority;
import org.milad.wallet.domain.user.User;
import org.milad.wallet.domain.user.UserService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid Username/Password"));

        if (!user.isAccountNonLocked()) {
            if (Duration.between(user.getLockTime(), Instant.now()).toMinutes() >= 15) {
                userService.resetFailedAttempts(user);
            }
        }

        List<String> authorities = user.getAuthorities().stream().map(Authority::getName).toList();

        return new CustomUserDetail(user.getId(), user.getUsername(), user.getPassword(), user.isEnabled(),
                true, true, user.isAccountNonLocked(),
                AuthorityUtils.createAuthorityList(authorities));
    }


}