package FraudDetection.service.internal.rules.impl;

import FraudDetection.dto.EvaluateRuleRequestDto;
import FraudDetection.dto.EvaluateRuleResponseDto;
import FraudDetection.enums.RuleEvaluationErrorStrategy;
import FraudDetection.enums.RuleEvaluationResult;
import FraudDetection.models.Event;
import FraudDetection.service.internal.rules.Rule;

import java.util.List;

public class VelocityCheckRule implements Rule {
    // The time window (in milliseconds) within which the events will be considered for the check
    private long timeDurationForFlagMillis;

    // The number of events exceeding which the rule will be triggered.
    private int numEventsToFlag;

    public VelocityCheckRule(long timeDurationForFlagMillis, int numEventsToFlag) {
        this.timeDurationForFlagMillis = timeDurationForFlagMillis;
        this.numEventsToFlag = numEventsToFlag;
    }

    @Override
    public EvaluateRuleResponseDto evaluate(EvaluateRuleRequestDto requestDto) {
        List<Event> previousEvents = requestDto.getPreviousEvents();
        Event currentEvent = requestDto.getCurrentEvent();

        previousEvents.add(currentEvent);
        previousEvents.sort((Event a, Event b) -> {
            if (a.getEventTimestamp().before(b.getEventTimestamp())) {
                return -1;
            } else if (a.getEventTimestamp().after(b.getEventTimestamp())) {
                return 1;
            }
            return 0;
        });

        long durationWindowOfEventsMillis = Math.abs(
                previousEvents.getLast().getEventTimestamp().getTime() -
                previousEvents.getFirst().getEventTimestamp().getTime()
        );

        if (durationWindowOfEventsMillis > timeDurationForFlagMillis) {
            return new EvaluateRuleResponseDto(RuleEvaluationResult.PASS, RuleEvaluationErrorStrategy.CONTINUE_EXECUTION);
        }
        if (previousEvents.size() > numEventsToFlag) {
            return new EvaluateRuleResponseDto(RuleEvaluationResult.FAIL, RuleEvaluationErrorStrategy.CONTINUE_EXECUTION);
        }
        return new EvaluateRuleResponseDto(RuleEvaluationResult.PASS, RuleEvaluationErrorStrategy.CONTINUE_EXECUTION);
    }
}
