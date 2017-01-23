package ubb.ro.templatetwo.list;

import java.util.List;

import ubb.ro.templatetwo.Event;

/**
 * Created by calin on 23.01.2017.
 */

public interface EventViewInterface {
    void showCompleted();
    void showError(String msg);
    void showEvents(List<Event> events);
}
