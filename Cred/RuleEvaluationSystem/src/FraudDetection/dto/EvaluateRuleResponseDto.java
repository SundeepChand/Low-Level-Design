package FraudDetection.dto;

import FraudDetection.enums.RuleEvaluationErrorStrategy;
import FraudDetection.enums.RuleEvaluationResult;

public class EvaluateRuleResponseDto {
    private RuleEvaluationResult ruleEvaluationResult;
    private RuleEvaluationErrorStrategy ruleEvaluationErrorStrategy;

    public EvaluateRuleResponseDto(RuleEvaluationResult ruleEvaluationResult, RuleEvaluationErrorStrategy ruleEvaluationErrorStrategy) {
        this.ruleEvaluationResult = ruleEvaluationResult;
        this.ruleEvaluationErrorStrategy = ruleEvaluationErrorStrategy;
    }

    public RuleEvaluationResult getRuleEvaluationResult() {
        return ruleEvaluationResult;
    }

    public RuleEvaluationErrorStrategy getRuleEvaluationFailureStrategy() {
        return ruleEvaluationErrorStrategy;
    }
}
