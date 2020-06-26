package com.codegun.kakaopay.interfaces.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
public class ReceivedCompletedInfoResDTO {
    private String receivedUserId;
    private long receivedAmount;

    @Builder
    public ReceivedCompletedInfoResDTO(String receivedUserId, long receivedAmount) {
        this.receivedUserId = receivedUserId;
        this.receivedAmount = receivedAmount;
    }
}
