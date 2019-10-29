package app;

import config.UMFConfig;
import helpers.OutputBuffer;
import org.anized.umf.app.InputOutput;
import org.anized.umf.app.Main;
import org.anized.umf.commands.Processor;
import org.anized.umf.services.ManifestService;
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
        UMFConfig.class, ManifestService.class, Processor.class, Main.class})
class AppIntegrationTest {

    @Autowired
    private OutputBuffer outputBuffer;

    @Test
    @DisplayName("app processes its commands, handles errors")
    void runApp() {
    }

    @AfterEach
    void verify() {
        assertEquals(expectedOutput, outputBuffer.getLines());
    }

    private static final List<String> expectedOutput = new ArrayList<>();
    static {
        final String red = InputOutput.Red;
        final String reset = InputOutput.Reset;
        expectedOutput.add("Enter command ('?' for help): add    Add Person (id, firstName, surname)");
        expectedOutput.add("quit   Terminate program");
        expectedOutput.add("count  Count Number of Persons");
        expectedOutput.add("delete Delete Person (id)");
        expectedOutput.add("edit   Edit Person (firstName, surname)");
        expectedOutput.add("upload Load Person manifest from xml file");
        expectedOutput.add("help   Display command help");
        expectedOutput.add("list   List Persons");
        expectedOutput.add("Enter command ('?' for help): Enter Person (id, firstname, surname): Person User, Test (ID=10134) successfully added");
        expectedOutput.add("Enter command ('?' for help): Enter Person (id, firstname, surname): "
                +red+"Person user, test already exists in manifest with id=10134"+reset);
        expectedOutput.add("Enter command ('?' for help): # Person records=1");
        expectedOutput.add("Enter command ('?' for help): Enter Person (firstname, surname): "
                +red+"Person known, not does not exist"+reset);
        expectedOutput.add("Enter command ('?' for help): Enter Person (firstname, surname): Enter new firstName (return to use user): Enter new surname (return to use test): updated Person: Changed, Name (ID=10134)");
        expectedOutput.add("Enter command ('?' for help): Enter Person (id, firstname, surname): "
                +red+"invalid Person invalid person, please enter: id, firstname, surname"+reset);
        expectedOutput.add("Enter command ('?' for help): Enter Person (firstname, surname): " +
                ""+red+"Person abcde does not exist"+reset);
        expectedOutput.add("Enter command ('?' for help): Enter Person Id: deleted Person 10134");
        expectedOutput.add("Enter command ('?' for help): Enter Person Id: "+red+"invalid Person id: ABCDE"+reset);
        expectedOutput.add("Enter command ('?' for help): Enter path to Person XML file: loaded 2 Person records of 2");
        expectedOutput.add("Enter command ('?' for help): Enter path to Person XML file: "
                +red+"failed to load Person records from no.such.file: Failed to read XML manifest file: no.such.file"+reset);
        expectedOutput.add("Enter command ('?' for help): Joe, Schmo (ID=123456)");
        expectedOutput.add("Susan, Schwachsin (ID=123460)");
        expectedOutput.add("Enter command ('?' for help): ");
    }

}