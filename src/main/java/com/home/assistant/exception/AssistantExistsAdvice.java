package com.home.assistant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class AssistantExistsAdvice {

  @ExceptionHandler(AssistantExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String assistantExistsHandler(AssistantExistsException ex) {
    return ex.getMessage();
  }
}

