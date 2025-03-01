package FraudDetection.models;

import java.time.ZonedDateTime;

public class Transaction {
    String userId;
    double amount;
    String location;
    String deviceId;
    ZonedDateTime txnTime;
    // Should be an enum, taking as string due to time constraint
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getTxnTime() {
        return txnTime;
    }

    public String getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public String getLocation() {
        return location;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Transaction(String userId, double amount, String location, String deviceId, ZonedDateTime txnTime) {
        this.userId = userId;
        this.amount = amount;
        this.location = location;
        this.deviceId = deviceId;
        this.txnTime = txnTime;
    }
}

