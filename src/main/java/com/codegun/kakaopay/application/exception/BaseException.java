package com.codegun.kakaopay.application.exception;

public class BaseException extends RuntimeException {

    private int errorCode;
    public BaseException(int errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public int errorCode() {
        return this.errorCode;
    }
}
