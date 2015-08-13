package com.bugsnag;

import com.bugsnag.transports.Transport;

public class Client {
    private Configuration config;

    /**
     * Initialize a Bugsnag client
     *
     * @param  apiKey   your Bugsnag API key from your Bugsnag dashboard
     */
    public Client(String apiKey) {
        if(apiKey == null) {
            throw new NullPointerException("You must provide a Bugsnag API key");
        }

        config = new Configuration(apiKey);
    }

    /**
     * Set the application version sent to Bugsnag.
     *
     * @param  appVersion  the app version to send
     */
    public void setAppVersion(String appVersion) {
        config.appVersion = appVersion;
    }

    /**
     * Set the method of delivery for Bugsnag events. By default we'll send
     * reports asynchronously using a thread pool to https://notify.bugsnag.com,
     * but you can ovverride this to use a different sending technique or
     * endpoint (for example, if you are using Bugsnag On-Premise).
     *
     * @param  transport  the transport mechanism to use
     * @see    Transport
     */
    public void setTransport(Transport transport) {
        config.transport = transport;
    }

    /**
     * Set which keys should be filtered when sending metaData to Bugsnag.
     * Use this when you want to ensure sensitive information, such as passwords
     * or credit card information is stripped from metaData you send to Bugsnag.
     * Any keys in metaData which contain these strings will be marked as
     * [FILTERED] when send to Bugsnag.
     *
     * For example:
     *
     *     client.setFilters("password", "credit_card");
     *
     * @param  filters  a list of keys to filter from metaData
     */
    public void setFilters(String... filters) {
        config.filters = filters;
    }

    /**
     * Set which exception classes should be ignored (not sent) by Bugsnag.
     *
     * For example:
     *
     *     client.setIgnoreClasses("java.lang.RuntimeException");
     *
     * @param  ignoreClasses  a list of exception classes to ignore
     */
    public void setIgnoreClasses(String... ignoreClasses) {
        config.ignoreClasses = ignoreClasses;
    }

    /**
     * Set for which releaseStages errors should be sent to Bugsnag.
     * Use this to stop errors from development builds being sent.
     *
     * For example:
     *
     *     client.setNotifyReleaseStages("production");
     *
     * @param  notifyReleaseStages  a list of releaseStages to notify for
     * @see    #setReleaseStage
     */
    public void setNotifyReleaseStages(String... notifyReleaseStages) {
        config.notifyReleaseStages = notifyReleaseStages;
    }

    /**
     * Set which packages should be considered part of your application.
     * Bugsnag uses this to help with error grouping, and stacktrace display.
     *
     * For example:
     *
     *     client.setProjectPackages("com.example.myapp");
     *
     * By default, we'll mark the current package name as part of you app.
     *
     * @param  projectPackages  a list of package names
     */
    public void setProjectPackages(String... projectPackages) {
        config.projectPackages = projectPackages;
    }

    /**
     * Set the current "release stage" of your application.
     *
     * @param  releaseStage  the release stage of the app
     * @see    #setNotifyReleaseStages
     */
    public void setReleaseStage(String releaseStage) {
        config.releaseStage = releaseStage;
    }

    /**
     * Add a callback to execute code before/after every notification to Bugsnag.
     *
     * You can use this to add or modify information attached to an error
     * before it is sent to your dashboard. You can also stop any events being
     * sent to Bugsnag completely.
     *
     * For example:
     *
     *     client.addCallback(new Callback() {
     *         @Override
     *         public boolean beforeNotify(Event event) {
     *             error.setSeverity(Severity.INFO);
     *         }
     *     })
     *
     * @param  callback  a callback to run before sending errors to Bugsnag
     * @see    Callback
     */
    public void addCallback(Callback callback) {
        config.addCallback(callback);
    }

    /**
     * Notify Bugsnag of a handled exception
     *
     * @param  throwable  the exception to send to Bugsnag
     */
    public void notify(Throwable throwable) {
        Event event = new Event(config, throwable);
        notify(event);
    }

    /**
     * Notify Bugsnag of a handled exception
     *
     * @param  throwable  the exception to send to Bugsnag
     * @param  severity   the severity of the error, one of Severity.ERROR,
     *                    Severity.WARNING or Severity.INFO
     */
    public void notify(Throwable throwable, Severity severity) {
        Event event = new Event(config, throwable);
        event.setSeverity(severity);
        notify(event);
    }

    /**
     * Notify Bugsnag of a handled exception
     *
     * @param  exception  the exception to send to Bugsnag
     * @param  metaData   additional information to send with the exception
     */
    public void notify(Throwable throwable, MetaData metaData) {
        Event event = new Event(config, throwable);
        event.setMetaData(metaData);
        notify(event);
    }

    /**
     * Notify Bugsnag of a handled exception
     *
     * @param  exception  the exception to send to Bugsnag
     * @param  severity   the severity of the error, one of Severity.ERROR,
     *                    Severity.WARNING or Severity.INFO
     * @param  metaData   additional information to send with the exception
     */
    public void notify(Throwable throwable, Severity severity, MetaData metaData) {
        Event event = new Event(config, throwable);
        event.setSeverity(severity);
        event.setMetaData(metaData);
        notify(event);
    }

    private void notify(Event event) {
        Notification notification = new Notification(config);
        notification.addEvent(event);

        config.transport.send(notification);
    }
}
