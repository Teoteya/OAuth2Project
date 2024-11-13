package com.test.auth.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Builder
public class ExceptionDto {
  String message;
  HttpStatus httpStatus;
}
