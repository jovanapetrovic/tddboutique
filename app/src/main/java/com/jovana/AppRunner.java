package com.jovana;

import com.jovana.config.ConfigPackage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = ConfigPackage.class)
public class AppRunner {

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
}
