package com.example.demo.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dao.CourseRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LessonConverterTest {

  @InjectMocks
  LessonConverter lessonConverter;

  @Mock
  CourseRepository courseRepository;

  Course course = new Course(1L, "Петр", "Джава для профессионалов");
  Lesson lesson = new Lesson(1L,"Первый урок", "Описание",course);
  LessonDto lessonDto = new LessonDto(1L,"Первый урок", "Описание",1L);

  @Test
  void createLessonFromLessonDtoTest() {
    when(courseRepository.getById(1L)).thenReturn(course);
    Lesson lessonTest = lessonConverter.createLessonFromLessonDto(lessonDto);
    verify(courseRepository, times(1)).getById(1L);
    Assertions.assertEquals(1L, lessonTest.getId());
    Assertions.assertEquals("Первый урок", lessonTest.getTitle());
    Assertions.assertEquals("Описание", lessonTest.getText());
    Assertions.assertEquals(course, lessonTest.getCourse());
  }

  @Test
  void createLessonDtoFromLesson() {
    LessonDto lessonDtoTest = lessonConverter.createLessonDtoFromLesson(lesson);
    Assertions.assertEquals(1L, lessonDtoTest.getId());
    Assertions.assertEquals("Первый урок", lessonDtoTest.getTitle());
    Assertions.assertEquals("Описание", lessonDtoTest.getText());
    Assertions.assertEquals(course.getId(), lessonDtoTest.getCourseId());

  }
}