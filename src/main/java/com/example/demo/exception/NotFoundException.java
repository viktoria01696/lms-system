package com.example.demo.exception;

public class NotFoundException extends RuntimeException {

  private final Long id;

  public NotFoundException(Long id) {
    super(String.format("Error with course ID %s", id));
    this.id = id;
  }
}
