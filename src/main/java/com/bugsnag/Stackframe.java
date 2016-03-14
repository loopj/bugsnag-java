package com.bugsnag;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

class Stackframe {
    private Configuration config;
    private StackTraceElement el;

    Stackframe(Configuration config, StackTraceElement el) {
        this.config = config;
        this.el = el;
    }

    static List<Stackframe> getStacktrace(Configuration config, StackTraceElement[] stackTraceElements) {
        List<Stackframe> stacktrace = new ArrayList<Stackframe>();
        for(StackTraceElement el : stackTraceElements) {
            stacktrace.add(new Stackframe(config, el));
        }

        return stacktrace;
    }

    @JsonProperty("file")
    public String getFile() {
        return el.getFileName() == null ? "Unknown" : el.getFileName();
    }

    @JsonProperty("method")
    public String getMethod() {
        return el.getClassName() + "." + el.getMethodName();
    }

    @JsonProperty("lineNumber")
    public int getLineNumber() {
        return el.getLineNumber();
    }

    @JsonProperty("inProject")
    public boolean isInProject() {
        return config.inProject(el.getClassName());
    }
}
