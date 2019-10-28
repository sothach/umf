package helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OutputBuffer {
    private final ByteArrayOutputStream byteArrayOutputStream
            = new ByteArrayOutputStream();

    public PrintStream getPrintStream() {
        return new PrintStream(byteArrayOutputStream);
    }

    public List<String> getLines() {
        return readBuffer();
    }

    private List<String> readBuffer() {
        final List<String> result = new ArrayList<>();
        final byte[] bytes = byteArrayOutputStream.toByteArray();
        final ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while(true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            result.add(line);
        }
        return result;
    }
}
