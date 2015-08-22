package com.bugsnag.transports;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncTransport extends HttpTransport {
    private static final int DEFAULT_NUM_THREADS = 5;
    private static final int SHUTDOWN_TIMEOUT = 5000;

    protected Transport baseTransport;
    protected ExecutorService executorService;
    private boolean shuttingDown = false;

    public AsyncTransport() {
        this(null, null);
    }

    public AsyncTransport(Transport baseTransport) {
        this(baseTransport, null);
    }

    public AsyncTransport(ExecutorService executorService) {
        this(null, executorService);
    }

    public AsyncTransport(Transport baseTransport, ExecutorService executorService) {
        if(baseTransport == null) {
            this.baseTransport = new HttpTransport();
        } else {
            this.baseTransport = baseTransport;
        }

        if(executorService == null) {
            this.executorService = Executors.newFixedThreadPool(DEFAULT_NUM_THREADS);
        } else {
            this.executorService = executorService;
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("SHUTTING DOWN THE JVM");
                AsyncTransport.this.shutdown();
            }
        });
    }

    public void setBaseTransport(Transport baseTransport) {
        this.baseTransport = baseTransport;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void send(final Object object) {
        if(shuttingDown) return;

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                baseTransport.send(object);
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
