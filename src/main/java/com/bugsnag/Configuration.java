package com.bugsnag;

import com.bugsnag.callbacks.Callback;
import com.bugsnag.callbacks.DeviceCallback;
import com.bugsnag.callbacks.ServletCallback;
import com.bugsnag.transports.AsyncTransport;
import com.bugsnag.transports.Transport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Configuration {
    String apiKey;
    String appVersion;
    Transport transport;
    String[] filters = new String[]{"password"};
    String[] ignoreClasses;
    String[] notifyReleaseStages = null;
    String[] projectPackages;
    String releaseStage;
    Collection<Callback> callbacks = new ArrayList<Callback>();

    Configuration(String apiKey) {
        this.apiKey = apiKey;
        this.transport = new AsyncTransport();

        // TODO: How to detect projectPackages automatically?

        // Add built-in callbacks
        addCallback(new DeviceCallback());

        if (ServletCallback.isAvailable()) {
            addCallback(new ServletCallback());
        }
    }

    boolean shouldNotifyForReleaseStage(String releaseStage) {
        if (notifyReleaseStages == null) {
            return true;
        }

        List<String> stages = Arrays.asList(notifyReleaseStages);
        return stages.contains(releaseStage);
    }

    boolean shouldIgnoreClass(String className) {
        if (ignoreClasses == null) {
            return false;
        }

        List<String> classes = Arrays.asList(ignoreClasses);
        return classes.contains(className);
    }

    void addCallback(Callback callback) {
        callbacks.add(callback);
    }

    boolean inProject(String className) {
        if (projectPackages != null) {
            for (String packageName : projectPackages) {
                if (packageName != null && className.startsWith(packageName)) {
                    return true;
                }
            }
        }

        return false;
    }
}
