package org.cloud13th.threads.normal.threadlocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ThreadLocalContext {

    private static final InheritableThreadLocal<Map<String, String>> context = new InheritableThreadLocal<>();

    public ThreadLocalContext() {
        context.set(new HashMap<>());
    }

    public String get(String key) {
        return context.get().get(key);
    }

    public void put(String key, Object value) {
        context.get().put(key, value.toString());
    }

    public void clear() {
        Optional.ofNullable(context.get())
                .ifPresent(Map::clear);
    }
}
