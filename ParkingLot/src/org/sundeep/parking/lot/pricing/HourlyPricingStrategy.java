package org.sundeep.parking.lot.pricing;

import org.sundeep.parking.lot.models.Ticket;

import java.time.Duration;

public class HourlyPricingStrategy implements PricingStrategy {
    private final double hourlyRate;

    public HourlyPricingStrategy(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double getAmount(Ticket ticket) {
        return this.hourlyRate * Duration.between(
                ticket.getEntryTime(), ticket.getExitTime()
        ).toHours();
    }
}
