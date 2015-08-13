package com.bugsnag;

import java.util.ArrayList;
import java.util.List;

class Notification {
    public Notifier notifier = Notifier.getInstance();
    public List<Event> events = new ArrayList<Event>();

    private Configuration config;

    Notification(Configuration config) {
        this.config = config;
    }

    public String getApiKey() {
        return config.apiKey;
    }

    void addEvent(Event event) {
        events.add(event);
    }
}
