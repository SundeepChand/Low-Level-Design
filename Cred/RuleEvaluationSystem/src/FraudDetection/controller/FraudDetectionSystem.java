package FraudDetection.controller;
import FraudDetection.dto.ProcessEventRequestDto;
import FraudDetection.dto.ProcessEventResponseDto;
import FraudDetection.service.FraudDetectionService;

public class FraudDetectionSystem {
    private final FraudDetectionService fraudDetectionService;

    public FraudDetectionSystem(FraudDetectionService fraudDetectionService) {
        this.fraudDetectionService = fraudDetectionService;
    }

    public boolean processEvent(ProcessEventRequestDto processEventRequestDto) {
        ProcessEventResponseDto responseDto = fraudDetectionService.processEvent(processEventRequestDto);
        return responseDto.getIsSuspicious();
    }
}
