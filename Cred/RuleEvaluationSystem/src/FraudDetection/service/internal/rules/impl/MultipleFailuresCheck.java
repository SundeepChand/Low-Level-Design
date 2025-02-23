package FraudDetection.service.internal.rules.impl;

import FraudDetection.dto.EvaluateRuleRequestDto;
import FraudDetection.dto.EvaluateRuleResponseDto;
import FraudDetection.enums.RuleEvaluationErrorStrategy;
import FraudDetection.enums.RuleEvaluationResult;
import FraudDetection.models.Event;
import FraudDetection.models.Transaction;
import FraudDetection.models.TransactionEvent;
import FraudDetection.service.internal.rules.Rule;

import java.util.List;

public class MultipleFailuresCheck implements Rule {
    private int failureThreshold;

    public MultipleFailuresCheck(int failureThreshold) {
        this.failureThreshold = failureThreshold;
    }

    @Override
    public EvaluateRuleResponseDto evaluate(EvaluateRuleRequestDto requestDto) {
        List<Event> previousEvents = requestDto.getPreviousEvents();
        Event currentEvent = requestDto.getCurrentEvent();

        if (!(currentEvent instanceof TransactionEvent)) {
            return new EvaluateRuleResponseDto(RuleEvaluationResult.PASS, RuleEvaluationErrorStrategy.FAIL_EXECUTION);
        }

        int numFailedTxn = 0;
        // Check for failed txn in previous events
        for (Event previousEvent: previousEvents) {
            if (previousEvent instanceof TransactionEvent) {
                TransactionEvent event = (TransactionEvent) previousEvent;
                Transaction txn = (Transaction)(event.getPayload());
                if (txn.getStatus() == "FAIL") {
                    numFailedTxn++;
                }
            }
        }

        if (numFailedTxn > failureThreshold) {
            return new EvaluateRuleResponseDto(RuleEvaluationResult.FAIL, RuleEvaluationErrorStrategy.FAIL_EXECUTION);
        }

        TransactionEvent event = (TransactionEvent) currentEvent;
        Transaction txn = (Transaction)(event.getPayload());
        if (txn.getStatus() == "FAIL") {
            return new EvaluateRuleResponseDto(RuleEvaluationResult.FAIL, RuleEvaluationErrorStrategy.FAIL_EXECUTION);
        }

        return new EvaluateRuleResponseDto(RuleEvaluationResult.PASS, RuleEvaluationErrorStrategy.FAIL_EXECUTION);
    }
}
