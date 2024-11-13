package com.test.auth.exception;

public class UserValidationException extends RuntimeException {
  public UserValidationException(String errorMessage) {
    super(errorMessage);
  }
}