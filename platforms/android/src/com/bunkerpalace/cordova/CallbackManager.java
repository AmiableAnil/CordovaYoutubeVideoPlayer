package com.bunkerpalace.cordova;

import java.util.ArrayList;

public class CallbackManager {
    private static CallbackManager instance;
    private ArrayList<EventListener> listeners;

    private CallbackManager() {
        this.listeners = new ArrayList<>();
    }

    public static CallbackManager getInstance() {
        if (instance == null) {
            instance = new CallbackManager();
        }
        return instance;
    }

    public void registerListener(EventListener eventListener) {
        listeners.add(eventListener);
    }

    public void unregister(EventListener eventListener) {
        listeners.remove(eventListener);
    }

    public void publishEvents(String value) {
        if (listeners == null || listeners.size() == 0) {
            return;
        }

        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).consumeEvents(value);
        }
    }

    public interface EventListener {
        void consumeEvents(String actionType);
    }
}
