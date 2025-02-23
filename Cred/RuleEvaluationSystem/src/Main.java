import FraudDetection.controller.FraudDetectionSystem;
import FraudDetection.dto.ProcessEventRequestDto;
import FraudDetection.models.Event;
import FraudDetection.models.Transaction;
import FraudDetection.models.TransactionEvent;
import FraudDetection.service.FraudDetectionService;
import FraudDetection.service.internal.rules.Rule;
import FraudDetection.service.internal.rules.impl.LocationAnomalyCheck;
import FraudDetection.service.internal.rules.impl.MultipleFailuresCheck;
import FraudDetection.service.internal.rules.impl.VelocityCheckRule;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Rule[] rules = new Rule[]{
            new VelocityCheckRule(10 * 60 * 1000, 5),
            new LocationAnomalyCheck(),
            // Taking threshold as 0 for demo purpose
            new MultipleFailuresCheck(0),
    };
    private static FraudDetectionService fraudDetectionService = new FraudDetectionService(rules);
    private static FraudDetectionSystem fraudSystem = new FraudDetectionSystem(fraudDetectionService);

    public static void main(String[] args) {
        case1NoPreviousEvents();
        case2VelocityCheck();
        case3LocationCheck();
        case4FailForFailedTxn();
        case5MultiplePreviousFailed();
        case6PreviousTransactionsAreValid();
        case8VelocityAndLocationCheck();
    }

    private static void case1NoPreviousEvents() {
        List<Event> previousEvents = new ArrayList<>();

        Event currentEvent = new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(1000)));

        boolean isFraudulent = fraudSystem.processEvent(new ProcessEventRequestDto(
                previousEvents,
                currentEvent
        ));

        System.out.println("1: Is Fraudulent: " + isFraudulent);
    }

    private static void case2VelocityCheck() {

        List<Event> previousEvents = new ArrayList<>();
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600000))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600100))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600200))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600300))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600400))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(601000))));

        Event currentEvent = new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(603000)));

        boolean isFraudulent = fraudSystem.processEvent(new ProcessEventRequestDto(
                previousEvents,
                currentEvent
        ));

        System.out.println("2: Is Fraudulent: " + isFraudulent);
    }

    private static void case3LocationCheck() {
        List<Event> previousEvents = new ArrayList<>();
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600000))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600100))));

        Event currentEvent = new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(1000)));

        boolean isFraudulent = fraudSystem.processEvent(new ProcessEventRequestDto(
                previousEvents,
                currentEvent
        ));

        System.out.println("3: Is Fraudulent: " + isFraudulent);
    }

    private static void case4FailForFailedTxn() {

        List<Event> previousEvents = new ArrayList<>();

        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600000))));

        Transaction txn = new Transaction("123", 1000, "NY", "dev-xyz", new Date(1000));
        txn.setStatus("FAIL");
        Event currentEvent = new TransactionEvent("1", txn);

        boolean isFraudulent = fraudSystem.processEvent(new ProcessEventRequestDto(
                previousEvents,
                currentEvent
        ));

        System.out.println("4: Is Fraudulent: " + isFraudulent);
    }

    private static void case5MultiplePreviousFailed() {
        List<Event> previousEvents = new ArrayList<>();
        Transaction txn = new Transaction("123", 1000, "NY", "dev-xyz", new Date(600000));
        txn.setStatus("FAIL");
        previousEvents.add(new TransactionEvent("1", txn));

        Event currentEvent = new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(1000)));

        boolean isFraudulent = fraudSystem.processEvent(new ProcessEventRequestDto(
                previousEvents,
                currentEvent
        ));

        System.out.println("5: Is Fraudulent: " + isFraudulent);
    }

    private static void case6PreviousTransactionsAreValid() {
        List<Event> previousEvents = new ArrayList<>();
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600000))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(1200000))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(2400000))));

        Event currentEvent = new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(3400000)));

        boolean isFraudulent = fraudSystem.processEvent(new ProcessEventRequestDto(
                previousEvents,
                currentEvent
        ));

        System.out.println("6: Is Fraudulent: " + isFraudulent);
    }

    private static void case8VelocityAndLocationCheck() {
        List<Event> previousEvents = new ArrayList<>();
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600000))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600100))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "Russia", "dev-xyz", new Date(600200))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600300))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(600400))));
        previousEvents.add(new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(601000))));

        Event currentEvent = new TransactionEvent("1", new Transaction("123", 1000, "NY", "dev-xyz", new Date(1000)));

        boolean isFraudulent = fraudSystem.processEvent(new ProcessEventRequestDto(
                previousEvents,
                currentEvent
        ));

        System.out.println("8: Is Fraudulent: " + isFraudulent);
    }
}
