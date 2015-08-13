package com.bugsnag;

import java.util.List;

public class Exception {
    private Configuration config;
    private Throwable throwable;

    Exception(Configuration config, Throwable throwable) {
        this.config = config;
        this.throwable = throwable;
    }

    public String getErrorClass() {
        return throwable.getClass().getName();
    }

    public String getMessage() {
        return throwable.getLocalizedMessage();
    }

    public List<Stackframe> getStacktrace() {
        return Stackframe.getStacktrace(config, throwable.getStackTrace());
    }
}
