package com.bugsnag;

import org.junit.Test;

import com.bugsnag.transports.HttpTransport;
import com.bugsnag.transports.OutputStreamTransport;

public class ClientTest {
    @Test
    public void testSerialization() {
        Client client = new Client("3fd63394a0ec74ac916fbdf3110ed957");
        // client.setEndpoint("http://localhost:8000");
        // client.setTransport(new HttpTransport());
        client.setTransport(new OutputStreamTransport(System.out));
        client.addToTab("Account", "Paying Customer", true);

        client.addCallback(new Callback() {
            @Override
            public void beforeNotify(Event event) {
                event.addToTab("Account", "Name", "Acme Co");
                event.addToTab("Account", "password", "s3cr3t");
            }
        });

        client.notify(new RuntimeException("oops"), Severity.INFO);
    }
}
