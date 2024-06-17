package org.sundeep.parking.lot.pricing;

import org.sundeep.parking.lot.models.Ticket;

public interface PricingStrategy {
    public double getAmount(Ticket ticket);
}
