package com.codegun.kakaopay.application.exception;

public class NotEffectiveTimeException extends BaseException{

  public NotEffectiveTimeException(int errorCode) {
    super(errorCode, "be out of date time");
  }
}
