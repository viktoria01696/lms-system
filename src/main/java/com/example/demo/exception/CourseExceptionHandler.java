package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CourseExceptionHandler {

  @ExceptionHandler
  public ModelAndView notFoundExceptionHandler(NotFoundException ex) {
    ModelAndView modelAndView = new ModelAndView("WrongCourse");
    modelAndView.setStatus(HttpStatus.NOT_FOUND);
    return modelAndView;
  }
}
