package commands;

import org.anized.umf.commands.DataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static org.springframework.test.util.AssertionErrors.fail;

class DataLoaderSpec {
    private final ClassLoader classLoader = getClass().getClassLoader();
    private final File testFile =
            new File(Objects.requireNonNull(classLoader.getResource("test-users.xml")).getFile());

    @Test
    @DisplayName("Load XML user files")
    void loadXml() {
        DataLoader.loadFromXmlFile(testFile.getAbsolutePath())
            .onSuccess(users -> users.forEach(System.out::println))
            .onFailure(e -> {
                e.printStackTrace();
                fail(e.getMessage());
            });
    }
}
