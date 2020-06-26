package com.codegun.kakaopay.interfaces.dto.assembler;

import com.codegun.kakaopay.domain.seed.Seed;
import com.codegun.kakaopay.interfaces.dto.res.SeedingResDTO;



public class SeedingDTOAssembler {

  public static SeedingResDTO toDTO(Seed seed) {
    return SeedingResDTO.builder()
        .token(seed.getToken())
        .build();
  }

}
