package com.bugsnag.transports;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpTransport implements Transport {
    static final String DEFAULT_ENDPOINT = "https://notify.bugsnag.com";
    static final int DEFAULT_TIMEOUT = 5000;

    private String endpoint = DEFAULT_ENDPOINT;
    private int timeout = DEFAULT_TIMEOUT;

    public HttpTransport() {}

    public HttpTransport(String endpoint) {
        this.endpoint = endpoint;
    }

    public HttpTransport(String endpoint, int timeout) {
        this.endpoint = endpoint;
        this.timeout = timeout;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void send(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);

        HttpURLConnection connection = null;
        try {
            URL url = new URL(endpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(timeout);
            connection.addRequestProperty("Content-Type", "application/json");

            OutputStream outputStream = null;
            try {
                outputStream = connection.getOutputStream();
                mapper.writeValue(outputStream, object);
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (final IOException ioe) {}
            }

            // End the request, get the response code
            int status = connection.getResponseCode();
            if(status / 100 != 2) {
                throw new RuntimeException("Bad response when sending events to Bugsnag");
            }
        } catch(JsonProcessingException ex) {
            throw new RuntimeException("Could not serialize object");
        } catch (IOException e) {
            throw new RuntimeException("Could not send events to Bugsnag");
        } finally {
            connection.disconnect();
        }
    }
}
