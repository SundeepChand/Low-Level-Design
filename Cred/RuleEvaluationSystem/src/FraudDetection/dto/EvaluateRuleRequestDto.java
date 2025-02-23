package FraudDetection.dto;

import FraudDetection.models.Event;

import java.util.List;

public class EvaluateRuleRequestDto {
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

    public EvaluateRuleRequestDto(List<Event> previousEvents, Event currentEvent) {
        this.previousEvents = previousEvents;
        this.currentEvent = currentEvent;
    }

    private Event currentEvent;
}
