package com.home.assistant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class AssistantNotFoundAdvice {

  @ExceptionHandler(AssistantNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String assistantNotFoundHandler(AssistantNotFoundException ex) {
    return ex.getMessage();
  }
}

