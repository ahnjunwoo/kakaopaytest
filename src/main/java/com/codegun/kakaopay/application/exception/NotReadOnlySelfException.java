package com.codegun.kakaopay.application.exception;

public class NotReadOnlySelfException extends BaseException{

  public NotReadOnlySelfException(int errorCode) {
    super(errorCode, "You can not select if you are not yourself");
  }
}
