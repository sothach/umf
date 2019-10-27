package app;

import config.UMFConfig;
import helpers.OutputBuffer;
import org.anized.umf.app.Main;
import org.anized.umf.commands.Processor;
import org.anized.umf.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoConfiguration

@ActiveProfiles("test")
@AutoConfigureDataMongo
@Import(EmbeddedMongoAutoConfiguration.class)
@SpringBootTest(classes={
        UMFConfig.class, UserService.class, Processor.class, Main.class})
class AppIntegrationTest {

    @Autowired
    private OutputBuffer outputBuffer;

    @Test
    @DisplayName("app processes help, add and list commands")
    void runApp() {
    }

    @AfterEach
    void verify() {
        outputBuffer.getLines().forEach(line ->
                System.out.println("expectedOutput.add(\""+line+"\");"));
        assertEquals(expectedOutput, outputBuffer.getLines());
    }

    private static List<String> expectedOutput = new ArrayList<>();
    static {
        expectedOutput.add("Enter command ('?' for help): add    add a user");
        expectedOutput.add("quit   terminate program");
        expectedOutput.add("count  count stored users");
        expectedOutput.add("delete delete a user");
        expectedOutput.add("edit   edit a user");
        expectedOutput.add("upload upload user(s) from XML");
        expectedOutput.add("help   display command help");
        expectedOutput.add("list   list stored user");
        expectedOutput.add("Enter command ('?' for help): Enter User (id, surname, firstname): user 'User, Test (ID=10134)' successfully added");
        expectedOutput.add("Enter command ('?' for help): User, Test (ID=10134)");
        expectedOutput.add("Enter command ('?' for help): ");
    }

}