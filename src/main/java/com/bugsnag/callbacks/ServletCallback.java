package com.bugsnag.callbacks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bugsnag.Callback;
import com.bugsnag.Event;

import com.bugsnag.servlet.BugsnagServletRequestListener;

public class ServletCallback extends Callback {
    public void beforeNotify(Event event) {
        // Skip this callback unless "javax.servlet.Servlet" is loaded
        try {
            Class.forName("javax.servlet.Servlet", false, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            return;
        }

        HttpServletRequest request = BugsnagServletRequestListener.getServletRequest();
        if(request == null) return;

        // TODO: Set context
        // TODO: Add request data to metaData

        System.out.println("Still in ServletCallback");

        System.out.println(request.getRequestURL().toString());
        System.out.println(request.getMethod());

        Map parameters = new HashMap<>();
        for (Map.Entry<String, String[]> parameterMapEntry : request.getParameterMap().entrySet()) {
            parameters.put(parameterMapEntry.getKey(), Arrays.asList(parameterMapEntry.getValue()));
        }

        System.out.println(parameters.toString());
    }
}
