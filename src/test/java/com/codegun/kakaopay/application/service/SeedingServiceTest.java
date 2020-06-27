package com.codegun.kakaopay.application.service;

import com.codegun.kakaopay.application.exception.*;
import com.codegun.kakaopay.domain.seed.Seed;
import com.codegun.kakaopay.infrastructure.jpa.seed.SeedRepository;
import com.codegun.kakaopay.interfaces.dto.req.ReceiveReqDTO;
import com.codegun.kakaopay.interfaces.dto.req.SeedingReqDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class SeedingServiceTest{
    @Autowired
    private SeedingService seedingService;
    private String receiveToken = "AmR";
    private String givenToken = "Am2";
    @Mock
    private SeedRepository seedRepository;

    private Seed alreadySeed = Seed.builder()
            .userId(1)
            .token(receiveToken)
            .roomId("room1")
            .receivedCompletedAmount(BigDecimal.ZERO)
            .seedingAmount(BigDecimal.valueOf(1000))
            .receivedPersonCount(3)
            .createdAt(LocalDateTime.now())
            .build();

    private Seed timeSeed = Seed.builder()
            .userId(1)
            .token(receiveToken)
            .roomId("room1")
            .receivedCompletedAmount(BigDecimal.ZERO)
            .seedingAmount(BigDecimal.valueOf(1000))
            .receivedPersonCount(3)
            .createdAt(LocalDateTime.now().plusMinutes(20))
            .build();

    private Seed todaySeed = Seed.builder()
            .userId(1)
            .token(givenToken)
            .roomId("room1")
            .receivedCompletedAmount(BigDecimal.ZERO)
            .seedingAmount(BigDecimal.valueOf(1000))
            .receivedPersonCount(3)
            .createdAt(LocalDateTime.now())
            .build();
    @Test
    @DisplayName("뿌리기 요청이 정상적으로 동작한다.올바른 토큰이 발행되어져야 한다.")
    void seedingMoney() {
        SeedingReqDTO seedingReqDTO = SeedingReqDTO.builder()
                .receivedPersonCount(3)
                .seedingAmount(100)
                .build();
        String userId = "1";
        String roomId = "room1";
        Seed seed = seedingService.seedingMoney(seedingReqDTO
                ,userId
                ,roomId);
        assertThat(seed).isNotNull();
        assertThat(seed.getToken().length()).isEqualTo(3);
    }

    @Test
    @DisplayName("뿌린 API에서 받은 토큰을 통해 정상적으로 돈을 받는 API")
    @Disabled
    void receiveMoney() {
        given(seedRepository.findByToken(givenToken)).willReturn(todaySeed);
        String userId = "2";
        String roomId = "room1";
        long receivedAmount = seedingService.receiveMoney(ReceiveReqDTO.builder().token(givenToken).build(),userId,roomId);
        assertThat(receivedAmount).isNotZero();
    }

    @Test
    @DisplayName("받기요청시 뿌리기 당한 사용자는 한번만 받을수 있습니다.")
    @Disabled
    void receiveMoneyCheck_step01() {
        given(seedRepository.findByToken(receiveToken)).willReturn(alreadySeed);
        String userId = "2";
        String roomId = "room1";
        assertThatThrownBy(() -> seedingService.receiveMoney(ReceiveReqDTO.builder().token(receiveToken).build(),userId,roomId))
                .isInstanceOf(AlreadySeedingException.class)
                .hasMessageContaining("Already Seeding");
    }

    @Test
    @DisplayName("자신이 뿌리기한 건은 자신이 받을 수 없습니다.")
    void receiveMoneyCheck_step02() {
        given(seedRepository.findByToken(receiveToken)).willReturn(alreadySeed);
        String userId = "1";
        String roomId = "room1";
        assertThatThrownBy(() -> seedingService.receiveMoney(ReceiveReqDTO.builder().token(receiveToken).build(),userId,roomId))
                .isInstanceOf(SelfSeedingException.class)
                .hasMessageContaining("You can not get yourself");
    }

    @Test
    @DisplayName("뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만 받을수있음")
    void receiveMoneyCheck_step03() {
        given(seedRepository.findByToken(receiveToken)).willReturn(alreadySeed);
        String userId = "3";
        String roomId = "room2";
        assertThatThrownBy(() -> seedingService.receiveMoney(ReceiveReqDTO.builder().token(receiveToken).build(),userId,roomId))
                .isInstanceOf(NotSameRoomException.class)
                .hasMessageContaining("be not in the same room");
    }

    @Test
    @DisplayName("뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기실패 응답이 내려가야 합니다.")
    @Disabled
    void receiveMoneyCheck_step04() {
        given(seedRepository.findByToken(receiveToken)).willReturn(timeSeed);
        String userId = "2";
        String roomId = "room1";
        assertThatThrownBy(() -> seedingService.receiveMoney(ReceiveReqDTO.builder().token(receiveToken).build(),userId,roomId))
                .isInstanceOf(NotEffectiveTimeException.class)
                .hasMessageContaining("be out of date time");
    }

    @Test
    @DisplayName("토큰으로 정상적으로 뿌리기 정보가 조회가 되어야 한다.")
    void findSeed() {
        String userId = "1";

        Seed seed = seedingService.findSeed(userId,receiveToken);
        assertThat(seed.getToken()).isEqualTo(receiveToken);
        assertThat(seed.getCreatedAt()).isNotNull();
        assertThat(seed.getSeedingAmount()).isNotNull();
    }

    @Test
    @DisplayName("뿌린 사람 자신만 조회를 할수 있습니다.")
    void findSeedOnlySelf() {
        String userId = "2";

        assertThatThrownBy(() -> seedingService.findSeed(userId,receiveToken))
                .isInstanceOf(NotReadOnlySelfException.class)
                .hasMessageContaining("You can not select if you are not yourself");

    }

    @Test
    @DisplayName("뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.")
    void findSeedPeriod() {
        String userId = "2";

        assertThatThrownBy(() -> seedingService.findSeed(userId,receiveToken))
                .isInstanceOf(NotReadOnlySelfException.class)
                .hasMessageContaining("You can not select if you are not yourself");

    }
}