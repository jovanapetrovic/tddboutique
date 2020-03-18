package com.jovana;

import com.jovana.config.ConfigPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = ConfigPackage.class)
public class AppRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppRunner.class);
    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

    public static void main(String[] args) {
        SpringApplication.run(new Class[]{AppRunner.class}, args);
        
        System.out.println(
                "______ _____  ___ ________   __\n" +
                        "| ___ \\  ___|/ _ \\|  _  \\ \\ / /\n" +
                        "| |_/ / |__ / /_\\ \\ | | |\\ V /\n" +
                        "|    /|  __||  _  | | | | \\ /\n" +
                        "| |\\ \\| |___| | | | |/ /  | |\n" +
                        "\\_| \\_\\____/\\_| |_/___/   \\_/\n");
    }

    /**
     * If no profile has been configured, set by default the "dev" profile.
     */
    private static void checkActiveSpringProfile() {
        if (System.getProperty(SPRING_PROFILES_ACTIVE) == null){
            System.setProperty(SPRING_PROFILES_ACTIVE, "dev");
        }
        LOGGER.info("Active Spring profiles: " + System.getProperty(SPRING_PROFILES_ACTIVE));
    }
}
