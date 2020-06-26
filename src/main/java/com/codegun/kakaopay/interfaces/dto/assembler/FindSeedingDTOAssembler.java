package com.codegun.kakaopay.interfaces.dto.assembler;

import com.codegun.kakaopay.domain.seed.Seed;
import com.codegun.kakaopay.interfaces.dto.res.ReceivedCompletedInfoResDTO;
import com.codegun.kakaopay.interfaces.dto.res.SeedDetailResDTO;
import com.codegun.kakaopay.interfaces.dto.res.SeedingResDTO;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;


public class FindSeedingDTOAssembler {

  public static SeedDetailResDTO toDTO(Seed seed) {
    return SeedDetailResDTO.builder()
        .createdAt(seed.getCreatedAt())
        .receivedCompletedAmount(seed.getReceivedCompletedAmount().longValue())
        .seedingAmount(seed.getSeedingAmount().longValue())
            .receivedCompletedInfos(getReceivedCompletedInfos(seed))
        .build();
  }

  private static ReceivedCompletedInfoResDTO toReceivedCompletedInfoResDTO(BigDecimal receivedAmount,String receivedUserId) {
    return ReceivedCompletedInfoResDTO.builder()
            .receivedAmount(receivedAmount.longValue())
            .receivedUserId(receivedUserId)
            .build();
  }
  private static Set<ReceivedCompletedInfoResDTO> getReceivedCompletedInfos(Seed seed) {

  return seed.getReceivedCompletedInfos().stream()
          .map(receivedCompletedInfo -> toReceivedCompletedInfoResDTO(receivedCompletedInfo.getReceivedAmount(), receivedCompletedInfo.getReceivedUserId()))
          .collect(Collectors.toSet());

  }


}
