package com.example.demo.service;


import com.example.demo.domain.Course;

import java.util.List;

public interface CourseListerService {

    List<Course> coursesByAuthor(String name);

    List<Course> coursesByPrefix(String prefix);

    Course findCourseById(Long id);

    void saveCourse(Course course);

    void deleteCourse(Long id);

    Course getOneById(Long id);
}
