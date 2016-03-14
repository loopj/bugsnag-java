package com.bugsnag;

import com.fasterxml.jackson.annotation.JsonProperty;

class Notifier {
    public static final String NOTIFIER_NAME = "Bugsnag Java";
    public static final String NOTIFIER_VERSION = "2.0.0";
    public static final String NOTIFIER_URL = "https://github.com/bugsnag/bugsnag-java";

    @JsonProperty("name")
    public String getName() {
        return NOTIFIER_NAME;
    }

    @JsonProperty("version")
    public String getVersion() {
        return NOTIFIER_VERSION;
    }

    @JsonProperty("url")
    public String getUrl() {
        return NOTIFIER_URL;
    }
}
