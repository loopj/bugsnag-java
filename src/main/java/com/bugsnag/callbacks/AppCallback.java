package com.bugsnag.callbacks;

import com.bugsnag.Configuration;
import com.bugsnag.Event;

public class AppCallback extends Callback {
    private Configuration config;

    public AppCallback(Configuration config) {
        this.config = config;
    }

    @Override
    public void beforeNotify(Event event) {
        event.app.put("version", config.appVersion);
        event.app.put("releaseStage", config.releaseStage);
    }
}
