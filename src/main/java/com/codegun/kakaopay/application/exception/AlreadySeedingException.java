package com.codegun.kakaopay.application.exception;

public class AlreadySeedingException extends BaseException{

  public AlreadySeedingException(int errorCode) {
    super(errorCode, "Already Seeding");
  }
}
