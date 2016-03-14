package com.bugsnag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.bugsnag.util.FilterTransformer;
import com.bugsnag.util.MapMerger;

public class Event {
    private static final String PAYLOAD_VERSION = "2";

    private Configuration config;
    private Diagnostics clientDiagnostics;

    private Throwable throwable;
    private Severity severity = Severity.WARNING;
    private String groupingHash;
    private Diagnostics diagnostics = new Diagnostics();

    Event(Configuration config, Diagnostics clientDiagnostics, Throwable throwable) {
        this.config = config;
        this.clientDiagnostics = clientDiagnostics;
        this.throwable = throwable;
    }

    @JsonProperty("payloadVersion")
    public String getPayloadVersion() {
        return PAYLOAD_VERSION;
    }

    @JsonProperty("exceptions")
    public List<Exception> getExceptions() {
        List<Exception> exceptions = new ArrayList<Exception>();

        Throwable currentThrowable = throwable;
        while(currentThrowable != null) {
            exceptions.add(new Exception(config, currentThrowable));

            currentThrowable = currentThrowable.getCause();
        }

        return exceptions;
    }

    @JsonProperty("threads")
    public List<ThreadState> getThreads() {
        return ThreadState.getLiveThreads(config);
    }

    @JsonProperty("groupingHash")
    public String getGroupingHash() {
        return groupingHash;
    }

    @JsonProperty("severity")
    public Severity getSeverity() {
        return severity;
    }

    @JsonProperty("context")
    public String getContext() {
        return diagnostics.context != null ? diagnostics.context : clientDiagnostics.context;
    }

    @JsonProperty("app")
    public Map getApp() {
        return new MapMerger(clientDiagnostics.app, diagnostics.app).merge();
    }

    @JsonProperty("device")
    public Map getDevice() {
        return new MapMerger(clientDiagnostics.device, diagnostics.device).merge();
    }

    @JsonProperty("user")
    public Map getUser() {
        return new MapMerger(clientDiagnostics.user, diagnostics.user).merge();
    }

    @JsonProperty("metaData")
    public Map getMetaData() {
        // Merge metadata maps
        Map mergedMap = new MapMerger(clientDiagnostics.metaData.getProperties(), diagnostics.metaData.getProperties()).merge();

        // Apply filters
        return Maps.transformEntries(mergedMap, new FilterTransformer(config.filters));
    }

    public Event addToTab(String tabName, String key, Object value) {
        diagnostics.metaData.addToTab(tabName, key, value);
        return this;
    }

    public Event clearTab(String tabName) {
        diagnostics.metaData.clearTab(tabName);
        return this;
    }

    public Event setAppInfo(String key, Object value) {
        diagnostics.app.put(key, value);
        return this;
    }

    public Event setContext(String context) {
        diagnostics.context = context;
        return this;
    }

    public Event setDeviceInfo(String key, Object value) {
        diagnostics.device.put(key, value);
        return this;
    }

    public Event setGroupingHash(String groupingHash) {
        this.groupingHash = groupingHash;
        return this;
    }

    public Event setSeverity(Severity severity) {
        this.severity = severity;
        return this;
    }

    public Event setUser(String id, String email, String name) {
        diagnostics.user.put("id", id);
        diagnostics.user.put("email", email);
        diagnostics.user.put("name", name);
        return this;
    }

    public Event setUserId(String id) {
        diagnostics.user.put("id", id);
        return this;
    }

    public Event setUserEmail(String email) {
        diagnostics.user.put("email", email);
        return this;
    }

    public Event setUserName(String name) {
        diagnostics.user.put("name", name);
        return this;
    }
}
