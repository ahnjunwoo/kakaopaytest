package com.codegun.kakaopay.interfaces.dto.res;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReceiveResDTO {
    private long receivedAmount;

    @Builder
    public ReceiveResDTO(long receivedAmount) {
        this.receivedAmount = receivedAmount;
    }
}
