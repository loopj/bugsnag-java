package com.bugsnag;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import static org.junit.Assert.*;

public class ThreadLocalMapTest {
    @Test
    public void testStorage() throws InterruptedException {
        final AtomicBoolean failed = new AtomicBoolean(true);

        final ThreadLocalMap map = new ThreadLocalMap();
        map.put("context", "parent");
        map.put("something", "here");

        assertEquals("parent", map.get("context"));
        assertEquals("here", map.get("something"));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                map.put("context", "child");
                map.put("more", "child");

                assertEquals("child", map.get("context"));
                assertEquals("here", map.get("something"));

                failed.set(false);
            }
        });

        thread.start();
        thread.join();

        assertEquals(failed.get(), false);
    }
}
