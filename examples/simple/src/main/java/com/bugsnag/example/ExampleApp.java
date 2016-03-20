package com.bugsnag.example;

import java.util.Date;

import com.bugsnag.Client;
import com.bugsnag.Event;
import com.bugsnag.Severity;
import com.bugsnag.callbacks.Callback;

public class ExampleApp {
    public static void main(String[] args) {
        // Create a Bugsnag client
        Client bugsnag = new Client("3fd63394a0ec74ac916fbdf3110ed957");

        // Set some diagnostic data which will not change during the
        // lifecycle of the application
        bugsnag.addToTab("app", "git-sha", "47a9afd86752964b58ae0aaab6f3a9221c0d5f5e");
        bugsnag.setReleaseStage("staging");
        bugsnag.setAppVersion("1.0.1");

        // Create and attach a simple Bugsnag callback.
        // Use Callbacks to send custom diagnostic data which changes during
        // the lifecyle of your application
        bugsnag.addCallback(new Callback() {
            @Override
            public void beforeNotify(Event event) {
                event.addToTab("diagnostics", "timestamp", new Date());
                event.addToTab("customer", "name", "acme-inc");
                event.addToTab("customer", "paying", true);
                event.addToTab("customer", "spent", 1234);
            }
        });

        // Send a handled exception to Bugsnag
        try {
            throw new RuntimeException("Handled exception - default severity");
        } catch(RuntimeException e) {
            bugsnag.notify(e);
        }

        // Send a handled exception to Bugsnag with info severity
        try {
            throw new RuntimeException("Handled exception - INFO severity");
        } catch(RuntimeException e) {
            bugsnag.notify(e, Severity.INFO);
        }

        // Send a handled exception with custom MetaData
        try {
            throw new RuntimeException("Handled exception - custom metadata");
        } catch(RuntimeException e) {
            Event event = bugsnag.buildEvent(e)
                .setSeverity(Severity.WARNING)
                .addToTab("report", "something", "blah")
                .setContext("blergh");

            bugsnag.notify(event);
        }

        // Throw an exception
        throw new RuntimeException("Unhandled exception");
    }
}
