package com.bugsnag.callbacks;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

import com.bugsnag.Event;

public class DeviceCallback extends Callback {
    @Override
    public void beforeNotify(Event event) {
        event.setDeviceInfo("hostname", getHostname());
        event.setDeviceInfo("osName", System.getProperty("os.name"));
        event.setDeviceInfo("osVersion", System.getProperty("os.version"));
        event.setDeviceInfo("osArch", System.getProperty("os.arch"));
        event.setDeviceInfo("runtimeName", System.getProperty("java.runtime.name"));
        event.setDeviceInfo("runtimeVersion", System.getProperty("java.runtime.version"));
        event.setDeviceInfo("locale", Locale.getDefault());
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
