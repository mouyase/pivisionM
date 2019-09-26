package com.reiya.pixiv.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2016/4/14.
 */
public class TempData {
    private static final Map<String, Object> map = new HashMap<>();

    public static void put(String key, Object object) {
        map.put(key, object);
    }

    public static Object get(String key) {
        return map.remove(key);
    }
}
