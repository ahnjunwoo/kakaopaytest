package com.codegun.kakaopay.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class TokenGeneratorTest {
    private TokenGenerator tokenGenerator = new TokenGenerator();

    @Test
    @DisplayName("예측할수 없는 임의의 문자열 토큰 3자리를 정상 발급되어진다.")
    void generatorToken() {
        String token = tokenGenerator.pub();
        log.info("토큰값: {}",token);
        assertThat(token.length()).isEqualTo(3);
    }
}