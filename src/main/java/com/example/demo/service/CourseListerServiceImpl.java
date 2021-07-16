package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.domain.Course;
import com.example.demo.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CourseListerServiceImpl implements CourseListerService {

  private final CourseRepository courseRepository;

  @Autowired
  public CourseListerServiceImpl(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  @Override
  public List<Course> coursesByAuthor(String name) {
    List<Course> allCourses = courseRepository.findAll();
    return allCourses.stream().filter(course -> course.getAuthor().equals(name))
        .collect(Collectors.toList());
  }

  @Override
  public List<Course> coursesByPrefix(String prefix) {
    return courseRepository.findByTitleWithPrefix(prefix == null ? "" : prefix);
  }

  @Override
  public Course coursesById(Long id) {
    return courseRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(id));
  }

  @Override
  public void saveCourse(Course course) {
    courseRepository.save(course);
  }

  @Override
  public Course createCourse() {
    return new Course();
  }

  @Override
  public void deleteCourse(Long id) {
    courseRepository.delete(id);
  }
}
