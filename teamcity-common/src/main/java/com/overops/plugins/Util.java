package com.overops.plugins;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public final class Util {

    private static ObjectMapper mapper = new ObjectMapper();

    public static void close(final Closeable fw) {
        if (fw != null) {
            try {
                fw.close();
            } catch (IOException ignore) {
                // ignore
            }
        }
    }

    public static <T> Optional<String> objectToString(T object) {
        try {
            return Optional.of(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public static <T> Optional<T> stringToObject(InputStream stream, Class<T> clazz) {
        try {
            return Optional.of(mapper.readValue(stream, clazz));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}
