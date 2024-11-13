package com.reimnop.swtools.util;

import java.util.HashSet;
import java.util.Set;

public class SWTEvent<T> {
    public interface EventHandler<T> {
        void invoke(Object sender, T args);
    }

    private final Set<EventHandler<T>> listeners = new HashSet<>();

    public void addListener(EventHandler<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(EventHandler<T> listener) {
        listeners.remove(listener);
    }

    public void invoke(Object sender, T args) {
        for (var listener : listeners) {
            listener.invoke(sender, args);
        }
    }
}
