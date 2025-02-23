package FraudDetection.enums;

public enum RuleEvaluationResult {
    PASS,
    FAIL,
    // Denotes that there was an error in evaluating the rule
    // In case say some network failures happen
    ERROR
}
