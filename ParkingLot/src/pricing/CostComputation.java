package pricing;

import models.Ticket;

public abstract class CostComputation {
    private final PricingStrategy pricingStrategy;

    CostComputation(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public double getTotalCost(Ticket ticket) {
        return ticket.getParkingSpot().getFixedPrice() + pricingStrategy.getAmount(ticket);
    }
}
