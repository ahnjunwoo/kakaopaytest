package com.codegun.kakaopay.interfaces.dto.res;


public class ReceiveRes extends KakaoResultRes<ReceiveResDTO> {
    public ReceiveRes() {
    }

    public ReceiveRes(ReceiveResDTO actionRes){
        setData(actionRes);
    }
}
