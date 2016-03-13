package com.bugsnag.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bugsnag.Client;
import com.bugsnag.Severity;

public class ExampleServlet extends HttpServlet {
    private Client bugsnag;

    public ExampleServlet() {
        bugsnag = new Client("3fd63394a0ec74ac916fbdf3110ed957");
        bugsnag.addToTab("custom", "james", "awesome");
        bugsnag.setProjectPackages("com.bugsnag.example");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        // Throw an exception
        throw new ServletException("Unhandled exception");
    }
}
