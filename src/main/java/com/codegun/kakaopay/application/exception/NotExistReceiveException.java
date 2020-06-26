package com.codegun.kakaopay.application.exception;

public class NotExistReceiveException extends BaseException{

  public NotExistReceiveException(int errorCode) {
    super(errorCode, "NotExistReceive");
  }
}
