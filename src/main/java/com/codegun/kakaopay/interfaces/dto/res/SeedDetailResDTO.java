package com.codegun.kakaopay.interfaces.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class SeedDetailResDTO {
    private long seedingAmount;
    private long receivedCompletedAmount;
    private LocalDateTime createdAt;
    private Set<ReceivedCompletedInfoResDTO> receivedCompletedInfos;

    @Builder
    public SeedDetailResDTO(long seedingAmount, long receivedCompletedAmount, LocalDateTime createdAt, Set<ReceivedCompletedInfoResDTO> receivedCompletedInfos) {
        this.seedingAmount = seedingAmount;
        this.receivedCompletedAmount = receivedCompletedAmount;
        this.createdAt = createdAt;
        this.receivedCompletedInfos = receivedCompletedInfos;
    }
}
