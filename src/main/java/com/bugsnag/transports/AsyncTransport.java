package com.bugsnag.transports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncTransport extends HttpTransport {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTransport.class);
    private static final int SHUTDOWN_TIMEOUT = 5000;

    protected Transport baseTransport;
    protected ExecutorService executorService;
    private boolean shuttingDown = false;
    private final ShutdownHandler shutdownHandler = new ShutdownHandler();

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
        if (baseTransport == null) {
            this.baseTransport = new HttpTransport();
        } else {
            this.baseTransport = baseTransport;
        }

        if (executorService == null) {
            this.executorService = Executors.newSingleThreadExecutor();
        } else {
            this.executorService = executorService;
        }

        Runtime.getRuntime().addShutdownHook(shutdownHandler);
    }

    public void setBaseTransport(Transport baseTransport) {
        this.baseTransport = baseTransport;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void send(final Object object) {
        if (shuttingDown) {
            logger.warn("Not notifying - 'sending' threads are already shutting down");
            return;
        }

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
                logger.warn("Shutdown of 'sending' threads took too long - forcing a shutdown");
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            logger.warn("Shutdown of 'sending' threads was interrupted - forcing a shutdown");
            executorService.shutdownNow();
        }
    }

    private final class ShutdownHandler extends Thread {
        @Override
        public void run() {
            AsyncTransport.this.shutdown();
        }
    }
}
