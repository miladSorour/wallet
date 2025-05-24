package org.milad.wallet.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.milad.wallet.domain.authority.AuthorityDTO;

import java.util.Set;

@Data
public class UserDTO {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String fullName;

    private Boolean enabled = true;

    private int failedAttempts = 0;

    private boolean accountNonLocked = true;

    private Long version;

    @NotEmpty
    private Set<AuthorityDTO> authorities;
}