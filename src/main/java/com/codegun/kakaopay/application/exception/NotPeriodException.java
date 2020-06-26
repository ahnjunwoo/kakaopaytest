package com.codegun.kakaopay.application.exception;

public class NotPeriodException extends BaseException{

  public NotPeriodException(int errorCode) {
    super(errorCode, "be out of date");
  }
}
