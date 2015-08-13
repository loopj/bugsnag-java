package com.bugsnag;

import java.util.ArrayList;
import java.util.List;

public class Event {
    public Severity severity = Severity.WARNING;
    public String payloadVersion = "2";
    public String groupingHash;
    public MetaData metaData;
    public MetaData app;
    public MetaData appState;
    public MetaData device;
    public MetaData deviceState;
    public User user;

    private Configuration config;
    private Throwable throwable;
    private String context;

    Event(Configuration config, Throwable throwable) {
        this.config = config;
        this.throwable = throwable;
    }

    public List<Exception> getExceptions() {
        List<Exception> exceptions = new ArrayList<Exception>();

        Throwable currentThrowable = throwable;
        while(currentThrowable != null) {
            exceptions.add(new Exception(config, currentThrowable));

            currentThrowable = currentThrowable.getCause();
        }

        return exceptions;
    }

    public List<ThreadState> getThreads() {
        return ThreadState.getLiveThreads(config);
    }

    void setSeverity(Severity severity) {
        this.severity = severity;
    }

    void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }
}
