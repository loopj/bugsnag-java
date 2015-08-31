package com.bugsnag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapMergerTest {
    @Test
    public void testBasicMerge() {
        Map map1 = new HashMap();
        map1.put("key1", "value1");

        Map map2 = new HashMap();
        map2.put("key2", "value2");

        Map mergedMap = new MapMerger(map1, map2).merge();
        assertEquals("value1", mergedMap.get("key1"));
        assertEquals("value2", mergedMap.get("key2"));
    }

    @Test
    public void testOverrideMerge() {
        Map map1 = new HashMap();
        map1.put("key1", "value1");

        Map map2 = new HashMap();
        map2.put("key1", "value2");

        Map mergedMap = new MapMerger(map1, map2).merge();
        assertEquals("value2", mergedMap.get("key1"));
    }

    @Test
    public void testDeepMerge() {
        Map baseMap = new HashMap();
        baseMap.put("key", "fromBase");
        Map base = new HashMap();
        base.put("map", baseMap);

        Map overridesMap = new HashMap();
        overridesMap.put("key", "fromOverrides");
        Map overrides = new HashMap();
        overrides.put("map", overridesMap);

        Map merged = new MapMerger(base, overrides).merge();
        Map mergedMap = (Map)merged.get("map");
        assertEquals("fromOverrides", mergedMap.get("key"));
    }
}
