package com.bugsnag.callbacks;

import com.bugsnag.Event;
import com.bugsnag.servlet.BugsnagServletRequestListener;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class ServletCallback extends Callback {
    private static final String HEADER_X_FORWARDED_FOR = "X-FORWARDED-FOR";

    public static boolean isAvailable() {
        try {
            Class.forName("javax.servlet.ServletRequestListener", false,
                          ServletCallback.class.getClassLoader());

            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    public void beforeNotify(Event event) {
        // Check if we have any servlet request data available
        HttpServletRequest request = BugsnagServletRequestListener.getServletRequest();
        if (request == null) {
            return;
        }

        // Add request information to metaData
        event.addToTab("request", "url", request.getRequestURL().toString());
        event.addToTab("request", "method", request.getMethod());
        event.addToTab("request", "params", request.getParameterMap());
        event.addToTab("request", "clientIp", getClientIp(request));
        event.addToTab("request", "headers", getHeaderMap(request));

        // Set default context
        event.setContext(request.getMethod() + " " + request.getRequestURI().toString());
    }

    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwardedAddr = request.getHeader(HEADER_X_FORWARDED_FOR);
        if (forwardedAddr != null) {
            remoteAddr = forwardedAddr;
            int idx = remoteAddr.indexOf(',');
            if (idx > -1) {
                remoteAddr = remoteAddr.substring(0, idx);
            }
        }
        return remoteAddr;
    }

    private Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            map.put(key, request.getHeader(key));
        }

        return map;
    }
}
