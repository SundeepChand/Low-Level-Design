package FraudDetection.service.internal.rules.impl;

import FraudDetection.dto.EvaluateRuleRequestDto;
import FraudDetection.dto.EvaluateRuleResponseDto;
import FraudDetection.enums.RuleEvaluationErrorStrategy;
import FraudDetection.enums.RuleEvaluationResult;
import FraudDetection.models.Event;
import FraudDetection.service.internal.rules.Rule;

import java.util.List;

public class LocationAnomalyCheck implements Rule {
    @Override
    public EvaluateRuleResponseDto evaluate(EvaluateRuleRequestDto requestDto) {
        List<Event> previousEvents = requestDto.getPreviousEvents();
        Event currentEvent = requestDto.getCurrentEvent();

        // Currently assuming all txns for single user.
        if (previousEvents.isEmpty()) {
            return new EvaluateRuleResponseDto(RuleEvaluationResult.PASS, RuleEvaluationErrorStrategy.FAIL_EXECUTION);
        }

        String previousLocation = previousEvents.getFirst().getEventLocation();

        if (currentEvent.getEventLocation().equals(previousLocation)) {
            return new EvaluateRuleResponseDto(RuleEvaluationResult.PASS, RuleEvaluationErrorStrategy.FAIL_EXECUTION);
        }
        return new EvaluateRuleResponseDto(RuleEvaluationResult.FAIL, RuleEvaluationErrorStrategy.FAIL_EXECUTION);
    }
}
