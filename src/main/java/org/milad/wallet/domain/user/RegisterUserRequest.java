package org.milad.wallet.domain.user;

import lombok.Data;

@Data
public class RegisterUserRequest { private String username, password, fullName; }
