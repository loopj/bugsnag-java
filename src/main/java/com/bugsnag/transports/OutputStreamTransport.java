package com.bugsnag.transports;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class OutputStreamTransport implements Transport {
    private OutputStream outputStream;

    public OutputStreamTransport(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void send(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(outputStream, object);
        } catch(JsonProcessingException ex) {
            throw new RuntimeException("Could not serialize object");
        } catch(IOException ex) {
            throw new RuntimeException("Could not access stream when serializing");
        }
    }
}
