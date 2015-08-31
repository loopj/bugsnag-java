package com.bugsnag;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ForwardingMap;

class ThreadLocalMap<K, V> extends ForwardingMap<K,V> {
    private InheritableThreadLocal tlm = new InheritableThreadLocal() {
        @Override
        public Object childValue(Object parentValue) {
            HashMap<K,V> map = (HashMap<K,V>) parentValue;
            if(map != null) {
                return map.clone();
            } else {
                return null;
            }
        }
    };

    public ThreadLocalMap() {
        tlm.set(new HashMap<K,V>());
    }

    protected Map<K,V> delegate() {
        Map<K,V> baseMap = (Map<K,V>) tlm.get();

        if(baseMap == null) {
            throw new NullPointerException("Null baseMap in ThreadLocalMap");
        }

        return baseMap;
    }
}
