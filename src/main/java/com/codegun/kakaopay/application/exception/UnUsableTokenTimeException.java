package com.codegun.kakaopay.application.exception;

public class UnUsableTokenTimeException extends BaseException{

  public UnUsableTokenTimeException(int errorCode, String token) {
    super(errorCode, "seeding is only valid for 10 minutes. Token["+token+"]");
  }
}
