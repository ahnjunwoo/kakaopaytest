package com.codegun.kakaopay.interfaces.dto.res;


public class SeedingRes extends KakaoResultRes<SeedingResDTO> {
    public SeedingRes() {
    }

    public SeedingRes(SeedingResDTO actionRes){
        setData(actionRes);
    }
}
