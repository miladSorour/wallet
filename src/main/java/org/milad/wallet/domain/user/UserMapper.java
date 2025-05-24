package org.milad.wallet.domain.user;

import org.milad.wallet.domain.authority.Authority;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static User mapToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // Don't encode here
        user.setFullName(dto.getFullName());
        user.setEnabled(dto.getEnabled());
        user.setVersion(dto.getVersion());
        user.setFailedAttempts(dto.getFailedAttempts());
        user.setAccountNonLocked(dto.isAccountNonLocked());

        if (dto.getAuthorities() != null) {
            Set<Authority> authorities = dto.getAuthorities().stream()
                    .map(a -> {
                        Authority auth = new Authority();
                        auth.setId(a.getId());
                        auth.setName(a.getName());
                        return auth;
                    })
                    .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        return user;
    }

}
