package com.bugsnag.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapMerger {
    private Map[] maps;
    private boolean deep = true;

    public MapMerger(Map... maps) {
        this.maps = maps;
    }

    public Map merge() {
        Map result = new HashMap();

        for(Map map : maps) {
            if(map == null) continue;

            // Get a set of all possible keys in base and overrides
            Set allKeys = new HashSet(result.keySet());
            allKeys.addAll(map.keySet());

            for(Object key : allKeys) {
                Object baseValue = result.get(key);
                Object overridesValue = map.get(key);

                if(overridesValue != null) {
                    if(deep && baseValue != null && baseValue instanceof Map && overridesValue instanceof Map) {
                        // Both original and overrides are Maps, go deeper
                        result.put(key, new MapMerger((Map)baseValue, (Map)overridesValue).merge());
                    } else {
                        result.put(key, overridesValue);
                    }
                } else {
                    // No collision, just use base value
                    result.put(key, baseValue);
                }
            }
        }

        return result;
    }
}
