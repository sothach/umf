package org.anized.umf.config;

import org.anized.umf.app.ConsoleInOut;
import org.anized.umf.app.InputOutput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Scanner;

@Configuration
@EnableMongoRepositories("org.anized.umf.persistence")
@Profile("production")
public class UMFConfig {

    @Bean
    protected InputOutput console() { return new ConsoleInOut(new Scanner(System.in), System.out);}

}