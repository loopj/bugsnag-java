package com.bugsnag;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

//
// TODO:JS - Add support for filters
//

public class MetaData {
    private static final String FILTERED_PLACEHOLDER = "[FILTERED]";
    private static final String OBJECT_PLACEHOLDER = "[OBJECT]";

    private String[] filters;
    private final Map<String, Object> store;

    public MetaData() {
        store = new ConcurrentHashMap<String, Object>();
    }

    public MetaData(Map<String, Object> m) {
        store = new ConcurrentHashMap<String, Object>(m);
    }

    @JsonAnyGetter
    public Map<String, Object> getStore() {
      return store;
    }

    public void addToTab(String tabName, String key, Object value) {
        Map<String, Object> tab = getTab(tabName);

        if(value != null) {
            tab.put(key, value);
        } else {
            tab.remove(key);
        }
    }

    public void clearTab(String tabName) {
        store.remove(tabName);
    }

    Map<String, Object> getTab(String tabName) {
        Map<String, Object> tab = (Map<String, Object>)store.get(tabName);

        if(tab == null) {
            tab = new ConcurrentHashMap<String, Object>();
            store.put(tabName, tab);
        }

        return tab;
    }

    void setFilters(String... filters) {
        this.filters = filters;
    }

    static MetaData merge(MetaData... metaDataList) {
        ArrayList<Map<String, Object>> stores = new ArrayList<Map<String, Object>>();
        for(MetaData metaData : metaDataList) {
            if(metaData != null) {
                stores.add(metaData.store);
            }
        }

        return new MetaData(mergeMaps(stores.toArray(new Map[0])));
    }

    private static Map<String, Object> mergeMaps(Map<String, Object>... maps) {
        Map<String, Object> result = new ConcurrentHashMap<String, Object>();

        for(Map<String, Object> map : maps) {
            if(map == null) continue;

            // Get a set of all possible keys in base and overrides
            Set<String> allKeys = new HashSet<String>(result.keySet());
            allKeys.addAll(map.keySet());

            for(String key : allKeys) {
                Object baseValue = result.get(key);
                Object overridesValue = map.get(key);

                if(overridesValue != null) {
                    if(baseValue != null && baseValue instanceof Map && overridesValue instanceof Map) {
                        // Both original and overrides are Maps, go deeper
                        result.put(key, mergeMaps((Map<String, Object>)baseValue, (Map<String, Object>)overridesValue));
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
