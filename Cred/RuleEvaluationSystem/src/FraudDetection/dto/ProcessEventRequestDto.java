package FraudDetection.dto;

import FraudDetection.models.Event;

import java.util.List;

public class ProcessEventRequestDto {
    // Currently we take the assumption that all the events belong to the same user
    // and based on that all the service logic is implemented.
    private List<Event> previousEvents;

    public List<Event> getPreviousEvents() {
        return previousEvents;
    }

    public void setPreviousEvents(List<Event> previousEvents) {
        this.previousEvents = previousEvents;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public ProcessEventRequestDto(List<Event> previousEvents, Event currentEvent) {
        this.previousEvents = previousEvents;
        this.currentEvent = currentEvent;
    }

    private Event currentEvent;


}
