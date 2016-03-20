package com.bugsnag;

import com.bugsnag.callbacks.Callback;
import com.bugsnag.transports.AsyncTransport;
import com.bugsnag.transports.HttpTransport;
import com.bugsnag.transports.Transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private Configuration config;
    private Diagnostics diagnostics = new Diagnostics();

    //
    // Constructors
    //

    /**
     * Initialize a Bugsnag client and automatically send uncaught exceptions.
     *
     * @param apiKey your Bugsnag API key from your Bugsnag dashboard
     */
    public Client(String apiKey) {
        this(apiKey, true);
    }

    /**
     * Initialize a Bugsnag client.
     *
     * @param apiKey your Bugsnag API key from your Bugsnag dashboard
     * @param sendUncaughtExceptions should we send uncaught exceptions to Bugsnag
     */
    public Client(String apiKey, boolean sendUncaughtExceptions) {
        if (apiKey == null) {
            throw new NullPointerException("You must provide a Bugsnag API key");
        }

        config = new Configuration(apiKey);

        // Automatically send unhandled exceptions to Bugsnag using this Client
        if (sendUncaughtExceptions) {
            ExceptionHandler.enable(this);
        }
    }


    //
    // Configuration
    //

    /**
     * Add a callback to execute code before/after every notification to Bugsnag.
     *
     * <p>You can use this to add or modify information attached to an error
     * before it is sent to your dashboard. You can also stop any events being
     * sent to Bugsnag completely.
     *
     * @param callback a callback to run before sending errors to Bugsnag
     * @see Callback
     */
    public void addCallback(Callback callback) {
        config.addCallback(callback);
    }

    /**
     * Set the endpoint to deliver Bugsnag events to. This is a convenient
     * shorthand for setTransport(new AsyncTransport(new HttpTransport(endpoint)));
     *
     * @param endpoint the endpoint to send events to
     * @see #setTransport
     */
    public void setEndpoint(String endpoint) {
        config.transport = new AsyncTransport(new HttpTransport(endpoint));
    }

    /**
     * Set which keys should be filtered when sending metaData to Bugsnag.
     * Use this when you want to ensure sensitive information, such as passwords
     * or credit card information is stripped from metaData you send to Bugsnag.
     * Any keys in metaData which contain these strings will be marked as
     * [FILTERED] when send to Bugsnag.
     *
     * @param filters a list of String keys to filter from metaData
     */
    public void setFilters(String... filters) {
        config.filters = filters;
    }

    /**
     * Set which exception classes should be ignored (not sent) by Bugsnag.
     *
     * @param ignoreClasses a list of exception classes to ignore
     */
    public void setIgnoreClasses(String... ignoreClasses) {
        config.ignoreClasses = ignoreClasses;
    }

    /**
     * Set for which releaseStages errors should be sent to Bugsnag.
     * Use this to stop errors from development builds being sent.
     *
     * @param notifyReleaseStages a list of releaseStages to notify for
     * @see #setReleaseStage
     */
    public void setNotifyReleaseStages(String... notifyReleaseStages) {
        config.notifyReleaseStages = notifyReleaseStages;
    }

    /**
     * Set which packages should be considered part of your application.
     * Bugsnag uses this to help with error grouping, and stacktrace display.
     *
     * @param projectPackages a list of package names
     */
    public void setProjectPackages(String... projectPackages) {
        config.projectPackages = projectPackages;
    }

    /**
     * Set the current "release stage" of your application.
     *
     * @param releaseStage  the release stage of the app
     * @see #setNotifyReleaseStages
     */
    public void setReleaseStage(String releaseStage) {
        diagnostics.app.put("releaseStage", releaseStage);
        config.releaseStage = releaseStage;
    }

    /**
     * Set the method of delivery for Bugsnag events. By default we'll send
     * reports asynchronously using a thread pool to https://notify.bugsnag.com,
     * but you can override this to use a different sending technique or
     * endpoint (for example, if you are using Bugsnag On-Premise).
     *
     * @param transport the transport mechanism to use
     * @see Transport
     */
    public void setTransport(Transport transport) {
        config.transport = transport;
    }


    //
    // Diagnostics
    //

    /**
     * Add diagnostic information to every error report.
     * Diagnostic information is collected in "tabs" on your dashboard.
     *
     * @param tab the dashboard tab to add diagnostic data to
     * @param key the name of the diagnostic information
     * @param value the contents of the diagnostic information
     */
    public void addToTab(String tab, String key, Object value) {
        diagnostics.metaData.addToTab(tab, key, value);
    }

    /**
     * Remove a tab of app-wide diagnostic information.
     *
     * @param tabName the dashboard tab to remove diagnostic data from
     */
    public void clearTab(String tabName) {
        diagnostics.metaData.clearTab(tabName);
    }

    /**
     * Set the application version sent to Bugsnag.
     *
     * @param appVersion the app version to send
     */
    public void setAppVersion(String appVersion) {
        diagnostics.app.put("version", appVersion);
    }

    /**
     * Set information about the end-user currently using the application.
     *
     * @param id the id of the current user
     * @param email the email address of the current user
     * @param name the name of the current user
     */
    public void setUser(String id, String email, String name) {
        diagnostics.user.put("id", id);
        diagnostics.user.put("email", email);
        diagnostics.user.put("name", name);
    }

    /**
     * Set the id of the end-user currently using the application.
     *
     * @param id the id of the current user
     */
    public void setUserId(String id) {
        diagnostics.user.put("id", id);
    }

    /**
     * Set the email address of the end-user currently using the application.
     *
     * @param email the email address of the current user
     */
    public void setUserEmail(String email) {
        diagnostics.user.put("email", email);
    }

    /**
     * Set the name of the end-user currently using the application.
     *
     * @param name the name of the current user
     */
    public void setUserName(String name) {
        diagnostics.user.put("name", name);
    }


    //
    // Notification
    //

    /**
     * Build an Event object to send to Bugsnag.
     *
     * @param throwable the exception to send to Bugsnag
     * @return the event object
     *
     * @see Event
     * @see #notify(com.bugsnag.Event)
     */
    public Event buildEvent(Throwable throwable) {
        return new Event(config, diagnostics, throwable);
    }

    /**
     * Notify Bugsnag of a handled exception.
     *
     * @param throwable the exception to send to Bugsnag
     * @return true unless the event was ignored
     */
    public boolean notify(Throwable throwable) {
        Event event = buildEvent(throwable);
        return notify(event);
    }

    /**
     * Notify Bugsnag of a handled exception.
     *
     * @param throwable the exception to send to Bugsnag
     * @param severity the severity of the error, one of {#link Severity#ERROR},
     *                 {@link Severity#WARNING} or {@link Severity#INFO}
     * @return true unless the event was ignored
     */
    public boolean notify(Throwable throwable, Severity severity) {
        Event event = buildEvent(throwable);
        event.setSeverity(severity);
        return notify(event);
    }

    /**
     * Notify Bugsnag of an exception and provide custom diagnostic data
     * for this particular event.
     *
     * @param event the {@link Event} object to send to Bugsnag
     * @return true unless the event was ignored
     *
     * @see Event
     * @see #buildEvent
     */
    public boolean notify(Event event) {
        // Don't notify if this error class should be ignored
        if (config.shouldIgnoreClass(event.getExceptionName())) {
            logger.info("Not notifying - exception class is in 'ignoreClasses'");
            return false;
        }

        // Don't notify unless releaseStage is in notifyReleaseStages
        if (!config.shouldNotifyForReleaseStage()) {
            logger.info("Not notifying - 'releaseStage' is not in 'notifyReleaseStages'");
            return false;
        }

        // Run beforeNotify callbacks
        for (Callback callback : config.callbacks) {
            try {
                // TODO: Have a way to halt execution based on callbacks
                //         - return false from callback (gross)
                //         - set event.shouldIgnore(true) in a callback
                //         - throw a special exception in a callback (Callback.HaltExecution?)
                callback.beforeNotify(event);
            } catch (Throwable ex) {
                logger.warn("Callback threw an exception in beforeNotify", ex);
            }
        }

        // Build the notification
        Notification notification = new Notification(config);
        notification.addEvent(event);

        // Deliver the notification
        logger.info("Notifying Bugsnag of an exception.");
        config.transport.send(notification);

        // Run afterNotify callbacks
        for (Callback callback : config.callbacks) {
            try {
                // Don't run further callbacls if callback.afterNotify returns false
                callback.afterNotify(event);
            } catch (Throwable ex) {
                logger.warn("Callback threw an exception in afterNotify", ex);
            }
        }

        return true;
    }
}
