package com.codegun.kakaopay.interfaces.dto.res;


public class FindReceiveRes extends KakaoResultRes<SeedDetailResDTO> {
    public FindReceiveRes() {
    }

    public FindReceiveRes(SeedDetailResDTO actionRes){
        setData(actionRes);
    }
}
