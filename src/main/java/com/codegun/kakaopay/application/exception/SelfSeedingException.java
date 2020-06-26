package com.codegun.kakaopay.application.exception;

public class SelfSeedingException extends BaseException{

  public SelfSeedingException(int errorCode) {
    super(errorCode, "You can not get yourself");
  }
}
