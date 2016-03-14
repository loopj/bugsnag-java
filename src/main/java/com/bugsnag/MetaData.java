package com.bugsnag;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.HashMap;
import java.util.Map;

class MetaData {
    private Map tabs = new HashMap();

    public void addToTab(String tabName, String key, Object value) {
        Map tab = getTab(tabName);
        tab.put(key, value);
    }

    public void clearTab(String tabName) {
        tabs.remove(tabName);
    }

    @JsonAnyGetter
    public Map getProperties() {
        return tabs;
    }

    private Map getTab(String tabName) {
        Map tab = (Map) tabs.get(tabName);
        if (tab == null) {
            tab = new HashMap();
            tabs.put(tabName, tab);
        }

        return tab;
    }
}
