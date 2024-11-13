package com.test.auth.advice;

import com.test.auth.dto.ExceptionDto;
import com.test.auth.exception.UserNotFoundException;
import com.test.auth.exception.UserValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

  private static final String ERROR_MESSAGE = "{} was caught: ";

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ExceptionDto handleUserNotFoundException(UserNotFoundException e) {
    log.error(ERROR_MESSAGE,e.getClass().getSimpleName(), e);
    return ExceptionDto.builder()
      .message(e.getMessage())
      .httpStatus(HttpStatus.NOT_FOUND)
      .build();
  }

  @ExceptionHandler(UserValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionDto handleUserValidationException(UserValidationException e) {
    log.error(ERROR_MESSAGE,e.getClass().getSimpleName(), e);
    return ExceptionDto.builder()
      .message(e.getMessage())
      .httpStatus(HttpStatus.BAD_REQUEST)
      .build();
  }
}
