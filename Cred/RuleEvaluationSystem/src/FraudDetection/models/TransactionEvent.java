package FraudDetection.models;

public class TransactionEvent extends Event {
    public TransactionEvent(String eventId, Transaction transaction) {
        super(eventId, transaction.getLocation(), transaction.getDeviceId(), transaction, transaction.getTxnTime());
    }
}
