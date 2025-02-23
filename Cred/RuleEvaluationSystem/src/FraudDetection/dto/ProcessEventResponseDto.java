package FraudDetection.dto;

public class ProcessEventResponseDto {
    private boolean isSuspicious;

    public ProcessEventResponseDto(boolean isSuspicious) {
        this.isSuspicious = isSuspicious;
    }

    public boolean getIsSuspicious() {
        return isSuspicious;
    }
}
