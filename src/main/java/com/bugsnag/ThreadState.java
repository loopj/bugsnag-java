package com.bugsnag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

class ThreadState {
    private Configuration config;
    private Thread thread;
    private StackTraceElement[] stackTraceElements;

    public long getId() {
        return thread.getId();
    }

    public String getName() {
        return thread.getName();
    }

    public List<Stackframe> getStacktrace() {
        return Stackframe.getStacktrace(config, stackTraceElements);
    }

    ThreadState(Configuration config, Thread thread, StackTraceElement[] stackTraceElements) {
        this.config = config;
        this.thread = thread;
        this.stackTraceElements = stackTraceElements;
    }

    static List<ThreadState> getLiveThreads(Configuration config) {
        // Get current thread id (the crashing thread) and stacktraces for all live threads
        long crashingThreadId = Thread.currentThread().getId();
        Map<Thread,StackTraceElement[]> liveThreads = Thread.getAllStackTraces();

        // Sort threads by thread-id
        Object[] keys = liveThreads.keySet().toArray();
        Arrays.sort(keys, new Comparator<Object>(){
            public int compare(Object a, Object b) {
                return Long.valueOf(((Thread) a).getId()).compareTo(((Thread)b).getId());
            }
        });

        List<ThreadState> threads = new ArrayList<ThreadState>();
        for(int i = 0; i < keys.length; i++) {
            Thread thread = (Thread)keys[i];

            // Don't show the current stacktrace here. It'll point at this method
            // rather than at the point they crashed.
            if (thread.getId() == crashingThreadId) {
                continue;
            }

            ThreadState threadState = new ThreadState(config, thread, liveThreads.get(thread));
            threads.add(threadState);
        }

        return threads;
    }
}
