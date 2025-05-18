package com.home.assistant.exception;

public class AssistantExistsException extends RuntimeException {

  public AssistantExistsException(String name) {
    super("Assistant with name " + name + " already exists");
  }
}