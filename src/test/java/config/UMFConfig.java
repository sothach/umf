package config;

import helpers.OutputBuffer;
import org.anized.umf.app.ConsoleInOut;
import org.anized.umf.app.InputOutput;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

@TestConfiguration
@Profile("test")
@EnableMongoRepositories("org.anized.umf.persistence")
public class UMFConfig {
    private OutputBuffer outputBuffer = new OutputBuffer();
    private Scanner input = commandFile("console-input.txt");

    private Scanner commandFile(final String filename) {
        try {
            return new Scanner(new File
                    (Objects.requireNonNull(
                            this.getClass().getClassLoader().getResource(filename)).getFile()));
        } catch (final FileNotFoundException e) {
            throw new IllegalStateException(
                    "Failed to initialize test, could not read commands from "+filename, e);
        }
    }

    @Bean
    protected InputOutput console() {
        return new ConsoleInOut(input, outputBuffer.getPrintStream());
    }

    @Bean
    protected OutputBuffer outputBuffer() {
        return outputBuffer;
    }
}