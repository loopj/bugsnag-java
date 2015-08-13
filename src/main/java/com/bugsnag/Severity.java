package com.bugsnag;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Severity {
    ERROR   ("error"),
    WARNING ("warning"),
    INFO    ("info");

    private final String value;

    private Severity(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
