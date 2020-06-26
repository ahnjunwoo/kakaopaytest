package com.codegun.kakaopay.application.exception;

public class NotExistTokenException extends BaseException{

  public NotExistTokenException(int errorCode, String token) {
    super(errorCode, "NotExistToken. Token["+token+"]");
  }
}
