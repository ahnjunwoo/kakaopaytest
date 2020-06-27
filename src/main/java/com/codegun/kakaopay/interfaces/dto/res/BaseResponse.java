package com.codegun.kakaopay.interfaces.dto.res;


public class BaseResponse {
    int code;

    public BaseResponse(int code) {
        this.code = code;
    }

    public int BaseResponse() {
        return code;
    }

    public int getCode() {
        return code;
    }
}
