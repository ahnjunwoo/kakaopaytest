package com.codegun.kakaopay.interfaces.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
public class SeedingResDTO {
    private String token;

    @Builder
    public SeedingResDTO(String token) {
        this.token = token;
    }
}
