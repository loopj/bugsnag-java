package com.bugsnag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import com.bugsnag.util.FilterTransformer;
import com.bugsnag.util.MapMerger;

public class Event {
    public Severity severity = Severity.WARNING;
    public String payloadVersion = "2";
    public String groupingHash;
    public Map app = new HashMap();
    public Map device = new HashMap();
    public Map user = new HashMap();

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

    public void setGroupingHash(String groupingHash) {
        this.groupingHash = groupingHash;
    }

    public void setUser(String id, String email, String name) {
        // TODO
    }

    public void setUserId(String id) {
        // TODO
    }

    public void setUserEmail(String email) {
        // TODO
    }

    public void setUserEmail(String name) {
        // TODO
    }
}
