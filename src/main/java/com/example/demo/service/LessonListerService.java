package com.example.demo.service;

import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import java.util.List;

public interface LessonListerService {

  Lesson createLesson();

  void saveLesson(Lesson lesson);

  Lesson lessonById(Long id);

  void deleteLesson(Long id);

  List<LessonDto> getLessonsDto(Long id);

}
