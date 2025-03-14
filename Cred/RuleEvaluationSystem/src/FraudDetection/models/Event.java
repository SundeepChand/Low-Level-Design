package FraudDetection.models;

import java.time.ZonedDateTime;

public abstract class Event {
    private String eventId;
    private String eventLocation;
    private String deviceId;
    private Object payload;
    private ZonedDateTime eventTimestamp;

    public Event(String eventId, String eventLocation, String deviceId, Object payload, ZonedDateTime eventTimestamp) {
        this.eventId = eventId;
        this.eventLocation = eventLocation;
        this.deviceId = deviceId;
        this.payload = payload;
        this.eventTimestamp = eventTimestamp;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Object getPayload() {
        return payload;
    }

    public ZonedDateTime getEventTimestamp() {
        return eventTimestamp;
    }
}
