package com.codegun.kakaopay.interfaces.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode(callSuper=false)
@ToString
public class KakaoResultRes<T> extends BaseResponse {

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public KakaoResultRes() {
        super(ResponseCodes.OK);
        this.message = "success";
        data = null;
    }

    public KakaoResultRes(int code, String message, T data){
        super(code);
        this.message = message;
        this.data = data;
    }

    public KakaoResultRes(int code, String message){
        super(code);
        this.message = message;
    }

    public void setData(T data){
        this.message = "success";
        this.data = data;
    }
}
