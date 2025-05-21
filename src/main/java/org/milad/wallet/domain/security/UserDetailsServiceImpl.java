package org.milad.wallet.domain.security;

import lombok.RequiredArgsConstructor;
import org.milad.wallet.domain.user.User;
import org.milad.wallet.domain.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.findByUsername(username);
        return user.map(UserDetailModel::new).orElseThrow(() -> new UsernameNotFoundException("Invalid Username"));
    }
}