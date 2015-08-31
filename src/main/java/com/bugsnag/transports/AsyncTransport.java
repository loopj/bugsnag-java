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
                // TODO: Move to logger
                System.out.println("Shutdown took too long - forcing a shutdown now");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            // TODO: Move to logger
            System.out.println("Shutdown was interrupted - forcing a shutdown now");
            executorService.shutdownNow();
        } finally {

        }
    }
}
