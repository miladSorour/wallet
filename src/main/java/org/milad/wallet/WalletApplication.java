package org.milad.wallet;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.milad.wallet.common.UtilDefaultProfile;
import org.milad.wallet.config.WalletAppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

import static org.milad.wallet.config.WalletConstants.SPRING_PROFILE_DEVELOPMENT;
import static org.milad.wallet.config.WalletConstants.SPRING_PROFILE_PRODUCTION;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "wallet", version = "0.0.1"))
@Slf4j
public class WalletApplication {


    private final Environment env;

    public WalletApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WalletApplication.class);
        UtilDefaultProfile.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.hasText(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
                "\n---------------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}\n\t" +
                        "swagger: \t{}://localhost:{}{}swagger-ui/index.html\n---------------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles(),
                protocol,
                serverPort,
                contextPath
        );
    }

    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time.");
        }
    }
}
