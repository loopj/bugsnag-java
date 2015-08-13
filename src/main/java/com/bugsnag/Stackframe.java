package com.bugsnag;

import java.util.ArrayList;
import java.util.List;

public class Stackframe {
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

    public String getFile() {
        return el.getFileName() == null ? "Unknown" : el.getFileName();
    }

    public String getMethod() {
        return el.getClassName() + "." + el.getMethodName();
    }

    public int getLineNumber() {
        return el.getLineNumber();
    }

    public boolean isInProject() {
        return config.inProject(el.getClassName());
    }
}
