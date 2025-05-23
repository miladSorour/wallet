package org.milad.wallet.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegisterUserResponse {

    private String username, fullName;
}
