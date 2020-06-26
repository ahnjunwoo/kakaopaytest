package com.codegun.kakaopay.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class TokenGenerator {
    public String pub() {
        return RandomStringUtils.random(3,48,122,true,true);
    }
}
