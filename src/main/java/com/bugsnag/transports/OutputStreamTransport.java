package com.bugsnag.transports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamTransport implements Transport {
    private OutputStream outputStream;

    public OutputStreamTransport(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void send(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setVisibilityChecker(
                mapper.getVisibilityChecker().with(JsonAutoDetect.Visibility.NONE)
            );

        try {
            mapper.writeValue(outputStream, object);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Could not serialize object");
        } catch (IOException ex) {
            throw new RuntimeException("Could not access stream when serializing");
        }
    }
}
