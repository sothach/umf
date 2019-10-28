package org.anized.umf.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.anized.common.Try;
import org.anized.umf.model.Person;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataLoader {
    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final TypeReference<List<Person>> manifestListType =
            new TypeReference<List<Person>>() {};

    public static Try<List<Person>> loadFromXmlFile(final String fileName) {
        return Try.apply(() -> {
            try {
                final InputStream is = Files.newInputStream(Paths.get(fileName.trim()));
                return xmlMapper.readValue(is, manifestListType);
            } catch (final Exception e) {
                throw new IllegalStateException("Failed to read XML manifest file: "+fileName,e);
            }
        });
    }
}
