package models;

import lombok.*;
import pricing.CostComputationFactory;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ticket {
    @NonNull private Vehicle vehicle;
    @NonNull private ParkingSpot parkingSpot;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    private double totalAmount;

    public Ticket(@NonNull Vehicle vehicle, @NonNull ParkingSpot parkingSpot) {
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
    }

    public Ticket(@NonNull Vehicle vehicle, @NonNull ParkingSpot parkingSpot, LocalDateTime entryTime) {
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = entryTime;
    }

    public void calculateTotalAmount() {
        this.totalAmount = CostComputationFactory
                .getCostComputation(this)
                .getTotalCost(this);
    }
}
