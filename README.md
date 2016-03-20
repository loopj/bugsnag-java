# Bugsnag Notifier for Java

The Bugsnag Notifier for Java gives you instant notification of exceptions thrown from your Java apps. Any unhandled exceptions will trigger a notification to be sent to your Bugsnag project.

[Bugsnag](http://bugsnag.com/) captures errors in real-time from your web, mobile and desktop applications, helping you to understand and resolve them as fast as possible. [Create a free account](http://bugsnag.com/) to start capturing exceptions from your applications.

## Contents

Diagnostics
- App Version
- Release Stage
- MetaData

Callbacks
- Stop events being sent to Bugsnag
- Add runtime diagnostic data to event reports
- Event Reports
    - Context
    - Grouping Hash
    - MetaData
    - Severity
    - User

Network
- Asynchronous sending
- Endpoints (HttpTransport.setEndpoint)
- Proxy (HttpTransport.setProxy)
- Timeouts (HttpTransport.setTimeout)
- Transport interface

Thread safety

Automatically collected diagnostics
- Device information
- Servlet request data

Logging system integration
