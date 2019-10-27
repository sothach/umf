package commands;

import org.anized.umf.app.CommandLine;
import org.anized.umf.app.InputOutput;
import org.anized.umf.commands.Command;
import org.anized.umf.commands.Processor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.when;

class CommandLineSpec {

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private InputOutput console;

    @Mock
    private Processor processor;

    @InjectMocks
    private CommandLine subject;

    private String getCommandHelp() {
        return help.toString();
    }
    private Command help =
            new Command("help", "display help",  () -> {
                console.println(getCommandHelp());
                return true;
            });
   private Command quit = new Command( "quit",   "terminate program", () -> false);

   private Optional<String> helpOption = Optional.of("help");
   private Optional<String> quitOption = Optional.of("quit");

    @Test
    @DisplayName("The command line reads & executes commands")
    void readCommand() {
        when(processor.find(helpOption)).thenReturn(Optional.of(help));
        when(processor.find(quitOption)).thenReturn(Optional.of(quit));
        when(console.nextLine())
                .thenReturn(helpOption)
                .thenReturn(quitOption);

        subject.repl();
    }
}
