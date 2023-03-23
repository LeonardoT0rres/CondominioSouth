package com.easyvoteapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableScheduling
public class App {

    public static void main(String[] args) {

        SpringApplication.run(App.class, args);
    }

    //TODO retornar hr fim votação
    //TODO Ajustar timezone
}
