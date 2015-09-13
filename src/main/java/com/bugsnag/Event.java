package com.bugsnag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class Event {
    public Severity severity = Severity.WARNING;
    public String payloadVersion = "2";
    public String groupingHash;

    private Configuration config;
    private Throwable throwable;
    private String context;

    private MetaData metaData = new MetaData();

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

    public Map getMetaData() {
        // Merge metadata maps
        Map mergedMap = new MapMerger(config.metaData.getProperties(), metaData.getProperties()).merge();

        // Apply filters
        return Maps.transformEntries(mergedMap, new FilterTransformer(config.filters));
    }

    public String getContext() {
        return config.context != null ? config.context : this.context;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public void addToTab(String tabName, String key, Object value) {
        metaData.addToTab(tabName, key, value);
    }

    public void setContext(String context) {
        this.context = context;
    }
}
