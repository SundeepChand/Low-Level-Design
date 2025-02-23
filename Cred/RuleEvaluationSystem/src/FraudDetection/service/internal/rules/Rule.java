package FraudDetection.service.internal.rules;

import FraudDetection.dto.EvaluateRuleRequestDto;
import FraudDetection.dto.EvaluateRuleResponseDto;

public interface Rule {
    EvaluateRuleResponseDto evaluate(EvaluateRuleRequestDto requestDto);
}
