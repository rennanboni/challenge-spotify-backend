package com.challenge.spotify.handler;

import com.challenge.spotify.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(new ErrorResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
    return ResponseEntity.internalServerError().body(new ErrorResponseDto(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }
}
