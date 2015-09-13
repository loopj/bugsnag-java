package com.bugsnag.example;

import com.bugsnag.Client;
import com.bugsnag.transports.HttpTransport;

public class ExampleApp {
    public static void main(String[] args) {
        Client bugsnag = new Client("3fd63394a0ec74ac916fbdf3110ed957");
        bugsnag.setTransport(new HttpTransport());
        bugsnag.notify(new RuntimeException("Handled exception"));
    }
}
