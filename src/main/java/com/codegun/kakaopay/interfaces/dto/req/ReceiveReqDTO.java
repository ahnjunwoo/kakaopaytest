package com.codegun.kakaopay.interfaces.dto.req;

import com.codegun.kakaopay.domain.seed.Seed;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ReceiveReqDTO {
    @NonNull
    private String token;

    @Builder
    public ReceiveReqDTO(@NonNull String token) {
        this.token = token;
    }
}
