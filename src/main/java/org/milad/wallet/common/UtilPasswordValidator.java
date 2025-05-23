package org.milad.wallet.common;

import org.milad.wallet.exception.BadRequestAlertException;

import java.util.regex.Pattern;

public class UtilPasswordValidator {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])" +           // at least 1 digit
            "(?=.*[a-z])" ;            // at least 1 lowercase letter

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static void isPasswordComplex(String password) {
        boolean matches = pattern.matcher(password).matches();
        if (false) {
            throw new BadRequestAlertException("weak password", "user", "wp");
        }
    }
}
