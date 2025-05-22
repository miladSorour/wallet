package org.milad.wallet.common;


import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

import static org.milad.wallet.config.WalletConstants.SPRING_PROFILE_DEVELOPMENT;

/**
 * Utility class to load a Spring profile to be used as default
 * when there is no <code>spring.profiles.active</code> set in the environment or as command line argument.
 * If the value is not available in <code>application.yml</code> then <code>dev</code> profile will be used as default.
 */
public final class UtilDefaultProfile {

    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

    private UtilDefaultProfile() {
    }

    /**
     * Set a default to use when no profile is configured.
     *
     * @param app the Spring application
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        /*
         * The default profile to use when no other profiles are defined
         * This cannot be set in the <code>application.yml</code> file.
         * See https://github.com/spring-projects/spring-boot/issues/1219
         */
        defProperties.put(SPRING_PROFILE_DEFAULT, SPRING_PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }
}

