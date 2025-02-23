package FraudDetection.service;

import FraudDetection.dto.EvaluateRuleRequestDto;
import FraudDetection.dto.EvaluateRuleResponseDto;
import FraudDetection.dto.ProcessEventRequestDto;
import FraudDetection.dto.ProcessEventResponseDto;
import FraudDetection.enums.RuleEvaluationErrorStrategy;
import FraudDetection.enums.RuleEvaluationResult;
import FraudDetection.service.internal.rules.Rule;

public class FraudDetectionService {
    private Rule[] rules;

    public FraudDetectionService(Rule[] rules) {
        this.rules = rules;
    }

    public ProcessEventResponseDto processEvent(ProcessEventRequestDto processEventRequestDto) {
        if (rules == null) {
            return new ProcessEventResponseDto(false);
        }

        for (Rule rule: rules) {
            EvaluateRuleResponseDto responseDto = rule.evaluate(new EvaluateRuleRequestDto(
                    processEventRequestDto.getPreviousEvents(),
                    processEventRequestDto.getCurrentEvent()
            ));
            if (responseDto.getRuleEvaluationResult() == RuleEvaluationResult.ERROR && responseDto.getRuleEvaluationFailureStrategy() == RuleEvaluationErrorStrategy.FAIL_EXECUTION) {
                return generateProcessEventResponseFromEvaluateRuleResponse(responseDto);
            }
            if (responseDto.getRuleEvaluationResult() == RuleEvaluationResult.FAIL) {
                return generateProcessEventResponseFromEvaluateRuleResponse(responseDto);
            }
        }
        return new ProcessEventResponseDto(false);
    }

    private ProcessEventResponseDto generateProcessEventResponseFromEvaluateRuleResponse(EvaluateRuleResponseDto responseDto) {
        if (responseDto.getRuleEvaluationResult() == RuleEvaluationResult.FAIL) {
            return new ProcessEventResponseDto(true);
        }
        return new ProcessEventResponseDto(false);
    }
}
