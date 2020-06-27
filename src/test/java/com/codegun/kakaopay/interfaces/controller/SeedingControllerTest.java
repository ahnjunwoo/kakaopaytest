package com.codegun.kakaopay.interfaces.controller;

import com.codegun.kakaopay.application.service.SeedingService;
import com.codegun.kakaopay.domain.seed.Seed;
import com.codegun.kakaopay.interfaces.dto.req.SeedingReqDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static com.codegun.kakaopay.interfaces.dto.req.CommonHeader.X_USER_ID;
import static com.codegun.kakaopay.interfaces.dto.req.CommonHeader.X_ROOM_ID;
import static org.hamcrest.Matchers.hasItem;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient(timeout = "10000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class SeedingControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SeedingService seedingService;

    private String testToken = "AmR";
    private String roomId = "room1";
    private String userId = "1";
    private Seed testSeed = Seed.builder()
            .userId(1)
            .token(testToken)
            .roomId("room1")
            .receivedCompletedAmount(BigDecimal.ZERO)
            .seedingAmount(BigDecimal.valueOf(1000))
            .receivedPersonCount(3)
            .build();
    private final static String X_USER_ID_PARAMS = "1";
    private final static String X_ROOM_ID_PARAMS = "room1";

    private Seed findResSeed = Seed.builder()
            .userId(Long.parseLong(userId))
            .token(testToken)
            .roomId(roomId)
            .receivedCompletedAmount(BigDecimal.valueOf(1000))
            .seedingAmount(BigDecimal.valueOf(1000))
            .receivedPersonCount(3)
            .createdAt(LocalDateTime.now())
            .build();

    @BeforeEach
    void before() {
        given(seedingService.seedingMoney(SeedingReqDTO.builder().receivedPersonCount(3).seedingAmount(1000).build(),userId,roomId)).willReturn(testSeed);
        given(seedingService.findSeed(userId, testToken)).willReturn(findResSeed);
    }

    @Test
    @DisplayName("뿌리기 API")
    void seedingMoney() {
        webTestClient.post().uri("/v1/seeds")
                .header(X_USER_ID, X_USER_ID_PARAMS)
                .header(X_ROOM_ID, X_ROOM_ID_PARAMS)
                .bodyValue(SeedingReqDTO.builder()
                        .seedingAmount(1000)
                        .receivedPersonCount(3)
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$..token").value(hasItem(testToken));
    }

    @Test
    @DisplayName("받기 API")
    void receiveMoney() {
        webTestClient.put().uri("/v1/seeds")
                .header(X_USER_ID, X_USER_ID_PARAMS)
                .header(X_ROOM_ID, X_ROOM_ID_PARAMS)
                .bodyValue(SeedingReqDTO.builder()
                        .seedingAmount(1000)
                        .receivedPersonCount(3)
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName("조회 API")
    void findSeed() {
        webTestClient.get().uri("/v1/seeds?token=AmR")
                .header(X_USER_ID, X_USER_ID_PARAMS)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}