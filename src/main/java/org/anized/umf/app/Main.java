package org.anized.umf.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@ComponentScan("org.anized.umf")
public class Main
  implements CommandLineRunner {
    private final CommandLine commandLine;

    @Autowired
    public Main(final CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }
  
    @Override
    public void run(final String... args) {
        commandLine.repl();
    }

}