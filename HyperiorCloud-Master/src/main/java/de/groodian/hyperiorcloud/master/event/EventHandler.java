package de.groodian.hyperiorcloud.master.event;

import de.groodian.hyperiorcloud.master.Master;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventHandler {

    private List<Listener> listeners;

    public EventHandler() {
        listeners = new ArrayList<>();
    }

    public void registerListener(Listener listener) {
        if (listener != null) {
            listeners.add(listener);
        } else {
            Master.getInstance().getLogger().error("Could not register listener!", new IllegalArgumentException());
        }
    }

    public void unregisterListener(Listener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        } else {
            Master.getInstance().getLogger().error("Could not remove the listener!", new IllegalArgumentException());
        }
    }

    public void callEvent(Event event) {
        for (Listener listener : listeners) {
            for (Method method : listener.getClass().getMethods()) {
                if (method.isAnnotationPresent(EventListener.class)) {
                    if (method.getParameterCount() == 1) {
                        if (method.getParameterTypes()[0] == event.getClass()) {
                            try {
                                method.invoke(listener, event);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

}
