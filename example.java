import com.bugsnag.Callback;
import com.bugsnag.Client;
import com.bugsnag.Event;
import com.bugsnag.Severity;

// Create a client
Client bugsnag = new Client("apikey");

// Configure how we'll send stuff to Bugsnag
bugsnag.setEndpoint("https://notify.bugsnag.com"); // Shortcut for bugsnag.setTransport(new AsyncTransport("endpoint url"))
bugsnag.setTransport(new HttpTransport());
bugsnag.setTransport(new AsyncTransport());
bugsnag.setTransport(new OutputStreamTransport(System.out));

// Configure what is sent to Bugsnag
bugsnag.setNotifyReleaseStages("production", "staging");
bugsnag.setAutoNotify(true);
bugsnag.setFilters("password");
bugsnag.setIgnoreClasses("java.io.IOException", "com.example.Custom");
bugsnag.setSendThreads(true);

// Tell Bugsnag about your application
bugsnag.setReleaseStage("production");
bugsnag.setAppVersion("1.0.0");
bugsnag.setProjectPackages("com.bugsnag.example");

// Send extra information with each crash
bugsnag.addCallback(new Callback() {
    @Override
    public void beforeNotify(Event event) {
        // Set the user for this event
        event.setUser("123", "james@bugsnag.com", "James Smith");
        event.setUserId("123");
        event.getUserId();
        event.setUserEmail("james@bugsnag.com");
        event.getUserEmail();
        event.setUserName("James Smith");
        event.getUserName();

        // Set the context for this event
        event.setContext("BlahController");
        event.getContext();

        // Set grouping rules for this event
        event.setGroupingHash("...");
        event.getGroupingHash();

        // Set severity for this event
        event.setSeverity();
        event.getSeverity();

        // Stop sending this event, and run no further callbacks
        event.setShouldIgnore(true);
        event.getShouldIgnore();

        // Get the underlying throwable object
        event.getException();
    }

    @Override
    public void afterNotify(Event event) {
        System.out.println("Sent error to Bugsnag!");
    }
});

// Maybe something like this for collecting app/device data?
class AppCallback implements Callback {
    private Configuration config;

    AppCallback(Configuration config) {
        this.config = config;
    }

    @Override
    public void beforeNotify(Event event) {
        event.app.put("version", config.appVersion);
    }
}
bugsnag.addCallback(new AppCallback(config));

// Provide Bugsnag callbacks in other packages (spring request/session data, spring-security user info, etc)

// Send an error to Bugsnag
bugsnag.notify(new RuntimeException("oopsie"), Severity.INFO);
