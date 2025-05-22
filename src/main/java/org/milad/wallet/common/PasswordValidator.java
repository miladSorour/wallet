package org.milad.wallet.common;

import org.milad.wallet.exception.BadRequestAlertException;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])" +           // at least 1 digit
            "(?=.*[a-z])" +            // at least 1 lowercase letter
            "(?=.*[A-Z])" +            // at least 1 uppercase letter
            "(?=.*[@#$%^&+=!])" +      // at least 1 special character
            "(?=\\S+$).{8,}$";         // no whitespace and at least 8 characters

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static void isPasswordComplex(String password) {
        boolean matches = pattern.matcher(password).matches();
        if (!matches) {
            throw new BadRequestAlertException("weak password", "user", "wp");
        }
    }
}
