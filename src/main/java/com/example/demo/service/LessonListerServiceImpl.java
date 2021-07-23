package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.LessonRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import com.example.demo.exception.NotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonListerServiceImpl implements LessonListerService {

  private LessonRepository lessonRepository;

  @Autowired
  public LessonListerServiceImpl(LessonRepository lessonRepository) {
    this.lessonRepository = lessonRepository;
  }

  @Override
  public Lesson createLesson(){ return new Lesson(); }

  @Override
  public void saveLesson(Lesson lesson){
    lessonRepository.save(lesson);}

  @Override
  public Lesson lessonById(Long id) {
    return lessonRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(id));
  }

  @Override
  public void deleteLesson(Long id) {
    lessonRepository.deleteById(id);
  }

  @Override
  public List<LessonDto> getLessonsDto(Long id){
    return lessonRepository.findAllForLessonIdWithoutText(id);
  }


}
