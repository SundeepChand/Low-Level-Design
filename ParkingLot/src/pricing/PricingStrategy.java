package pricing;

import models.Ticket;

public interface PricingStrategy {
    public double getAmount(Ticket ticket);
}
