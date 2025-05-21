package org.milad.wallet.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 *
 * <p> Properties are configured in the application.yml file. </p>
 * <p> This class also load properties in the Spring Environment from the git.properties and META-INF/build-info.properties
 * files if they are found in the classpath.</p>
 */
@ConfigurationProperties(prefix = "wallet", ignoreUnknownFields = false)
@Getter
@Setter
@Component
public class WalletAppProperties {

    private Security security= new Security();

    @Setter
    @Getter
    public static class Security {
        private String contentSecurityPolicy = WalletDefaults.Security.contentSecurityPolicy;
    }

}

