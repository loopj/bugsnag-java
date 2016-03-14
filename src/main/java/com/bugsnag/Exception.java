package com.bugsnag;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

class Exception {
    private Configuration config;
    private Throwable throwable;

    Exception(Configuration config, Throwable throwable) {
        this.config = config;
        this.throwable = throwable;
    }

    @JsonProperty("errorClass")
    public String getErrorClass() {
        return throwable.getClass().getName();
    }

    @JsonProperty("message")
    public String getMessage() {
        return throwable.getLocalizedMessage();
    }

    @JsonProperty("stacktrace")
    public List<Stackframe> getStacktrace() {
        return Stackframe.getStacktrace(config, throwable.getStackTrace());
    }
}
