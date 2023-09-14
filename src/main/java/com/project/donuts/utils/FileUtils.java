package com.project.donuts.utils;

import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public String readFile(String fileName) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        Assert.notNull(inputStream, "File Not Found!");
        return readFromInputStream(inputStream);
    }

    private String readFromInputStream(InputStream inputStream) {
        StringBuilder resultStringBuilder = new StringBuilder();

        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, StandardCharsets.UTF_8))) {
            int c;
            while ((c = reader.read()) != -1) {
                resultStringBuilder.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resultStringBuilder.toString();
    }
}
