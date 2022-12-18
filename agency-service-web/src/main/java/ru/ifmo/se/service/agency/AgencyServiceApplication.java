package ru.ifmo.se.service.agency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class AgencyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgencyServiceApplication.class, args);
    }
}
