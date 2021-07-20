package com.example.demo;

import com.example.demo.domain.Course;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import  org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class CourseTests {
  private static final Validator validator;
  static {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.usingContext().getValidator();
  }

  @Test
  void shouldAvoidSpecialSymbols(){
    Course testCourse = new Course(1L, "Вася", "Программирование \r для чайников");
    Set<ConstraintViolation<Course>> validates = validator.validate(testCourse);
    validates.stream().map(v -> v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName()).collect(
        Collectors.toList());
    assertThat(validates.size()).isEqualTo(1);
  }

  @Test
  void shouldKeepJustOneWhitespaceBetweenWords(){
    Course testCourse = new Course(1L, "Вася", "Программирование    для чайников");
    Set<ConstraintViolation<Course>> validates = validator.validate(testCourse);
    validates.stream().map(v -> v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName()).collect(
        Collectors.toList());
    assertThat(validates.size()).isEqualTo(1);
  }

  @Test
  void shouldStartAndEndLineWithoutWhitespace(){
    Course testCourse = new Course(1L, "Вася", " Программирование для чайников ");
    Set<ConstraintViolation<Course>> validates = validator.validate(testCourse);
    validates.stream().map(v -> v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName()).collect(
        Collectors.toList());
    assertThat(validates.size()).isEqualTo(1);
  }

  @Test
  void shouldUseOnlyOneLanguage(){
    Course testCourse = new Course(1L, "Вася", "Программирование для dummies");
    Set<ConstraintViolation<Course>> validates = validator.validate(testCourse);
    validates.stream().map(v -> v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName()).collect(
        Collectors.toList());
    assertThat(validates.size()).isEqualTo(1);
  }

  @Test
  void shouldUseTitleWithNoNonAlphabeticSymbols(){
    Course testCourse = new Course(1L, "Вася", "Программирование для чайников!");
    Set<ConstraintViolation<Course>> validates = validator.validate(testCourse);
    validates.stream().map(v -> v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName()).collect(
        Collectors.toList());
    assertThat(validates.size()).isEqualTo(1);
  }
  @Test
  void shouldUseUppercaseForAllWordsExceptConjunctionsInEng(){
    Course testCourse = new Course(1L, "Вася", "Programming Is Not For Dummies");
    Set<ConstraintViolation<Course>> validates = validator.validate(testCourse);
    validates.stream().map(v -> v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName()).collect(
        Collectors.toList());
    assertThat(validates.size()).isEqualTo(1);
  }
  @Test
  void shouldUseUppercaseJustForFirsWordInRu(){
    Course testCourse = new Course(1L, "Вася", "Программирование Для чайников");
    Set<ConstraintViolation<Course>> validates = validator.validate(testCourse);
    validates.stream().map(v -> v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName()).collect(
        Collectors.toList());
    assertThat(validates.size()).isEqualTo(1);
  }
  @Test
  void shouldValidateForRu(){
    Course testCourse = new Course(1L, "Вася", "Программирование для чайников");
    Set<ConstraintViolation<Course>> validates = validator.validate(testCourse);
    validates.stream().map(v -> v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName()).collect(
        Collectors.toList());
    assertThat(validates.size()).isEqualTo(0);
  }
  @Test
  void shouldValidateForEn(){
    Course testCourse = new Course(1L, "Вася", "Programming Is not for Dummies");
    Set<ConstraintViolation<Course>> validates = validator.validate(testCourse);
    validates.stream().map(v -> v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName()).collect(
        Collectors.toList());
    assertThat(validates.size()).isEqualTo(0);
  }
}
