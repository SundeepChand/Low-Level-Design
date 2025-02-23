package FraudDetection.models;

public class ProfileUpdateEvent extends Event {
    public ProfileUpdateEvent(String eventId, ProfileUpdate profileUpdate) {
        // TODO: Fill this
        super(eventId, "", "", profileUpdate, null);
    }
}
