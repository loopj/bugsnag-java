package com.bugsnag;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bugsnag.transports.AsyncTransport;
import com.bugsnag.transports.Transport;

import com.bugsnag.callbacks.Callback;
import com.bugsnag.callbacks.ServletCallback;

/**
 * User-specified configuration storage object, contains information
 * specified at the client level.
 */
class Configuration {
    String apiKey;
    String appVersion;
    Transport transport;
    String[] filters = new String[]{"password"};
    String[] ignoreClasses;
    String[] notifyReleaseStages = null;
    String[] projectPackages;
    String releaseStage;

    MetaData metaData = new MetaData();
    String context;

    Collection<Callback> callbacks = new ArrayList<Callback>();

    Configuration(String apiKey) {
        this.apiKey = apiKey;
        this.transport = new AsyncTransport();

        // Add built-in callbacks
        addCallback(new ServletCallback());
    }

    boolean shouldNotifyForReleaseStage(String releaseStage) {
        if(notifyReleaseStages == null)
            return true;

        List<String> stages = Arrays.asList(notifyReleaseStages);
        return stages.contains(releaseStage);
    }

    boolean shouldIgnoreClass(String className) {
        if(ignoreClasses == null)
            return false;

        List<String> classes = Arrays.asList(ignoreClasses);
        return classes.contains(className);
    }

    void addCallback(Callback callback) {
        callbacks.add(callback);
    }

    boolean inProject(String className) {
        if(projectPackages != null) {
            for(String packageName : projectPackages) {
                if(packageName != null && className.startsWith(packageName)) {
                    return true;
                }
            }
        }

        return false;
    }
}
