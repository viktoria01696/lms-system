package com.example.demo.exception;

import java.security.Principal;

public class NotFoundException extends RuntimeException {

  private Long id;

  public NotFoundException(Long id) {
    super(String.format("Не найден курс с ID %s", id));
    this.id = id;
  }
  public NotFoundException(Principal principal) {
    super(String.format("Пользователь с логином %s не найден!", principal.getName()));
  }
  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(){};
}
