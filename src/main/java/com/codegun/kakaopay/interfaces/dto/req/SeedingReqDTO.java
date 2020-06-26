package com.codegun.kakaopay.interfaces.dto.req;

import com.codegun.kakaopay.domain.seed.Seed;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SeedingReqDTO {
    @NonNull
    private long seedingAmount;
    @NonNull
    private int receivedPersonCount;

    @Builder
    public SeedingReqDTO(long seedingAmount, int receivedPersonCount) {
        this.seedingAmount = seedingAmount;
        this.receivedPersonCount = receivedPersonCount;
    }

    public Seed toEntity(long userId, String roomId, String token) {
        return Seed.builder()
                .token(token)
                .userId(userId)
                .roomId(roomId)
                .receivedPersonCount(this.receivedPersonCount)
                .seedingAmount(BigDecimal.valueOf(this.seedingAmount))
                .receivedCompletedAmount(BigDecimal.ZERO)
                .build();
    }
}
