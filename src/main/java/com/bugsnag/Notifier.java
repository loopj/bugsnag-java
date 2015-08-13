package com.bugsnag;

class Notifier {
    public final String name = "Bugsnag Java";
    public final String version = "2.0.0";
    public final String url = "https://github.com/bugsnag/bugsnag-java";

    private static final Notifier instance = new Notifier();

    static Notifier getInstance() {
        return instance;
    }
}
