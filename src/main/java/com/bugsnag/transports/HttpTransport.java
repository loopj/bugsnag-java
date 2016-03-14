package com.bugsnag.transports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

public class HttpTransport implements Transport {
    protected static final String DEFAULT_ENDPOINT = "https://notify.bugsnag.com";
    protected static final int DEFAULT_TIMEOUT = 5000;

    protected String endpoint = DEFAULT_ENDPOINT;
    protected int timeout = DEFAULT_TIMEOUT;
    protected Proxy proxy;

    public HttpTransport() {

    }

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

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void send(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setVisibilityChecker(
                mapper.getVisibilityChecker().with(JsonAutoDetect.Visibility.NONE)
            );

        HttpURLConnection connection = null;
        try {
            URL url = new URL(endpoint);
            if (proxy != null) {
                connection = (HttpURLConnection) url.openConnection(proxy);
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }

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
                } catch (final IOException ioe) {
                    // Don't care
                }
            }

            // End the request, get the response code
            int status = connection.getResponseCode();
            if (status / 100 != 2) {
                throw new RuntimeException("Bad response when sending events to Bugsnag");
            }
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Could not serialize object", ex);
        } catch (IOException ex) {
            throw new RuntimeException("Could not send events to Bugsnag", ex);
        } finally {
            connection.disconnect();
        }
    }
}
