package com.home.assistant.exception;

public class AssistantNotFoundException extends RuntimeException {

  public AssistantNotFoundException(String name) {
    super("Could not find assistant with name " + name);
  }
}