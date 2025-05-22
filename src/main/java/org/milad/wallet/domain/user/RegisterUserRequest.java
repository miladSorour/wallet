package org.milad.wallet.domain.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterUserRequest {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String fullName;

}
