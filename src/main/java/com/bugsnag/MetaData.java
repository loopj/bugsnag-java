package com.bugsnag;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import com.bugsnag.util.ThreadLocalMap;

class MetaData {
    private Map tabs = new ThreadLocalMap();

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
        if(tab == null) {
            tab = new ThreadLocalMap();
            tabs.put(tabName, tab);
        }

        return tab;
    }
}
