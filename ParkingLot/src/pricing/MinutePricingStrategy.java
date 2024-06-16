package pricing;

import models.Ticket;

import java.time.Duration;

public class MinutePricingStrategy implements PricingStrategy {
    private final double perMinuteRate;

    public MinutePricingStrategy(double perMinuteRate) {
        this.perMinuteRate = perMinuteRate;
    }

    @Override
    public double getAmount(Ticket ticket) {
        return this.perMinuteRate * Duration.between(
                ticket.getEntryTime(), ticket.getExitTime()
        ).toMinutes();
    }
}
