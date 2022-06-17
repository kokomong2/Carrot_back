package com.carrot.carrot_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CarrotBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarrotBackApplication.class, args);
    }

}
