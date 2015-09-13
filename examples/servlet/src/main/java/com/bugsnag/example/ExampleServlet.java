package com.bugsnag.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bugsnag.Client;

public class ExampleServlet extends HttpServlet {
    private Client bugsnag;

    public ExampleServlet() {
        bugsnag = new Client("3fd63394a0ec74ac916fbdf3110ed957");
        bugsnag.addToTab("custom", "james", "awesome");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        bugsnag.notify(new RuntimeException("Something broke"));

        throw new ServletException("Something else went wrong!");
    }
}
