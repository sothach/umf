package org.anized.umf.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.anized.common.Try;
import org.anized.umf.model.User;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataLoader {
    private static XmlMapper xmlMapper = new XmlMapper();
    private static TypeReference<List<User>> userListType =
            new TypeReference<List<User>>() {};

    public static Try<List<User>> loadFromXmlFile(final String fileName) {
        return Try.apply(() -> {
            try {
                final InputStream is = Files.newInputStream(Paths.get(fileName.trim()));
                return xmlMapper.readValue(is, userListType);
            } catch (final Exception e) {
                throw new RuntimeException("Failed to read XML user file: "+fileName,e);
            }
        });
    }
}