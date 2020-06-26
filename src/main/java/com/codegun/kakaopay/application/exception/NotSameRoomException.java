package com.codegun.kakaopay.application.exception;

public class NotSameRoomException extends BaseException{

  public NotSameRoomException(int errorCode) {
    super(errorCode, "be not in the same room");
  }
}
