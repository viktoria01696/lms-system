package com.example.demo.service;

import com.example.demo.domain.Course;
import java.util.List;

public interface CourseListerService {

  List<Course> coursesByAuthor(String name);

  List<Course> coursesByPrefix(String prefix);

  Course coursesById(Long id);

  void saveCourse(Course course);

  Course createCourse();

  void deleteCourse(Long id);
}
