package com.codegun.kakaopay.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenGeneratorTest {
    private TokenGenerator tokenGenerator = new TokenGenerator();

    @Test
    @DisplayName("예측할수 없는 임의의 문자열 토큰 3자리를 정상 발급되어진다.")
    void generatorToken() {
        String token = tokenGenerator.pub();
        assertThat(token.length()).isEqualTo(3);
    }
}