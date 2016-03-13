package com.bugsnag.callbacks;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

import com.bugsnag.Event;

public class DeviceCallback extends Callback {
    @Override
    public void beforeNotify(Event event) {
        event.device.put("hostname", getHostname());
        event.device.put("osName", System.getProperty("os.name"));
        event.device.put("osVersion", System.getProperty("os.version"));
        event.device.put("osArch", System.getProperty("os.arch"));
        event.device.put("runtimeName", System.getProperty("java.runtime.name"));
        event.device.put("runtimeVersion", System.getProperty("java.runtime.version"));
        event.device.put("locale", Locale.getDefault());
    }

    private String getHostname() {
        // Windows always sets COMPUTERNAME
        if (System.getProperty("os.name").startsWith("Windows")) {
            return System.getenv("COMPUTERNAME");
        }

        // Try the HOSTNAME env variable (most unix systems)
        String hostname = System.getenv("HOSTNAME");
        if (hostname != null) {
           return hostname;
        }

        // Resort to dns hostname lookup
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            // Give up
        }

        return null;
    }
}
