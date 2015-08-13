package com.bugsnag.transports;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncTransport extends HttpTransport {
    private static final int DEFAULT_NUM_THREADS = 5;
    private static final int SHUTDOWN_TIMEOUT = 5000;

    private ExecutorService executorService;
    private boolean shuttingDown = false;

    public AsyncTransport() {
        this(Executors.newFixedThreadPool(DEFAULT_NUM_THREADS));
    }

    public AsyncTransport(ExecutorService executorService) {
        super();
        this.executorService = executorService;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("SHUTTING DOWN THE JVM");
                AsyncTransport.this.shutdown();
            }
        });
    }

    public void send(final Object object) {
        if(shuttingDown) return;

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                AsyncTransport.super.send(object);
            }
        });
    }

    private void shutdown() {
        shuttingDown = true;

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS)) {
                System.out.println("Graceful shutdown took too much time, forcing the shutdown.");
                executorService.shutdownNow();
            }
            System.out.println("Shutdown finished.");
        } catch (InterruptedException e) {
            System.out.println("Graceful shutdown interrupted, forcing the shutdown.");
            executorService.shutdownNow();
        } finally {
            // actualConnection.close();
        }
    }
}
