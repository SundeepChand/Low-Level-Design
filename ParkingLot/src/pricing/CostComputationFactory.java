package pricing;

import models.Ticket;

public class CostComputationFactory {
    private static final CostComputation bikeCostComputation = new TwoWheelerCostComputation(
            new HourlyPricingStrategy(10));
    private static final CostComputation carCostComputation = new FourWheelerCostComputation(
            new MinutePricingStrategy(0.5)
    );

    public static CostComputation getCostComputation(Ticket ticket) {
        return switch (ticket.getVehicle().getVehicleType()) {
            case CAR -> carCostComputation;
            case BIKE -> bikeCostComputation;
        };
    }
}
