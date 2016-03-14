package com.bugsnag;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

class Notification {
    private Configuration config;
    private List<Event> events = new ArrayList<Event>();

    Notification(Configuration config) {
        this.config = config;
    }

    @JsonProperty("apiKey")
    public String getApiKey() {
        return config.apiKey;
    }

    @JsonProperty("notifier")
    public Notifier getNotifier() {
        return new Notifier();
    }

    @JsonProperty("events")
    public List<Event> getEvents() {
        return events;
    }

    void addEvent(Event event) {
        events.add(event);
    }
}
