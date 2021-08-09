package com.example.demo.service;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.example.demo.dao.CourseRepository;
import com.example.demo.domain.Course;
import com.example.demo.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseListerServiceTest {

  @InjectMocks
  CourseListerServiceImpl courseListerServiceImpl ;

  @Mock
  CourseRepository courseRepository;

  static List<Course> allCourses;

  @BeforeAll
  public static void setUpWithCourseList(){
    allCourses = new ArrayList<>();
    Course courseTestFirst = new Course(1L, "Петр", "Джава для профессионалов");
    Course courseTestSecond = new Course(2L, "Веня", "Джава для отчаявшихся");
    Course courseTestThird = new Course(3L, "Анастасия", "Быть или не быть, вот в чем вопрос");
    allCourses.add(courseTestFirst);
    allCourses.add(courseTestSecond);
    allCourses.add(courseTestThird);
  }

  @Test
  void coursesByAuthorTest() {
    when(courseRepository.findAll()).thenReturn(allCourses);
    List<Course> coursesTest = courseListerServiceImpl.coursesByAuthor("Петр");
    verify(courseRepository, times(1)).findAll();
    Assertions.assertEquals(1, coursesTest.size());
  }

  @Test
  void coursesByPrefixTest() {
    List<Course> coursesByPrefix = new ArrayList<>();
    coursesByPrefix.add(allCourses.get(0));
    coursesByPrefix.add(allCourses.get(1));
    when(courseRepository.findByTitleLike("Джава%")).thenReturn(coursesByPrefix);
    List<Course> coursesTest = courseListerServiceImpl.coursesByPrefix("Джава");
    verify(courseRepository, times(1)).findByTitleLike("Джава%");
    Assertions.assertEquals(coursesByPrefix.size(), coursesTest.size());
  }

  @Test
  void findCourseByCorrectIdTest() {
    when(courseRepository.findById(1L)).thenReturn(Optional.of(allCourses.get(0)));
    Course courseTest = courseListerServiceImpl.findCourseById(1L);
    verify(courseRepository, times(1)).findById(1L);
    Assertions.assertEquals(1L, courseTest.getId());
    Assertions.assertEquals("Петр", courseTest.getAuthor());
    Assertions.assertEquals("Джава для профессионалов", courseTest.getTitle());
  }
  @Test
  void findCourseByWrongIdTest() {
    Assertions.assertThrows(NotFoundException.class, ()->{
    when(courseRepository.findById(10L)).thenThrow(NotFoundException.class);
    Course courseTest = courseListerServiceImpl.findCourseById(10L);
    verify(courseRepository, times(1)).findById(10L);
  });}

  @Test
  void saveCourseTest() {
    courseListerServiceImpl.saveCourse(allCourses.get(0));
    verify(courseRepository, times(1)).save(allCourses.get(0));
  }

  @Test
  void deleteCourseByCorrectIdTest() {
    courseListerServiceImpl.deleteCourse(1L);
    verify(courseRepository, times(1)).deleteById(1L);
  }
  @Test
  void deleteCourseByWrongIdTest() {
    Assertions.assertThrows(NotFoundException.class, ()->{
    doThrow(NotFoundException.class).when(courseRepository).deleteById(10L);
    courseListerServiceImpl.deleteCourse(10L);
    verify(courseRepository, times(1)).deleteById(10L);
    });}

  @Test
  void getOneByCorrectId() {
    when(courseRepository.getOne(1L)).thenReturn(allCourses.get(0));
    Course courseTest = courseListerServiceImpl.getOneById(1L);
    verify(courseRepository, times(1)).getOne(1L);
    Assertions.assertEquals(1L, courseTest.getId());
    Assertions.assertEquals("Петр", courseTest.getAuthor());
    Assertions.assertEquals("Джава для профессионалов", courseTest.getTitle());
  }
  @Test
  void getOneByWrongId() {
    Assertions.assertThrows(NotFoundException.class, ()->{
    when(courseRepository.getOne(10L)).thenThrow(NotFoundException.class);
    Course courseTest = courseListerServiceImpl.getOneById(10L);
    verify(courseRepository, times(1)).getOne(10L);
    });}
}