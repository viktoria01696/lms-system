package com.example.demo.converter;

import com.example.demo.dao.CourseRepository;
import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonConverter {

    private final CourseRepository courseRepository;

    @Autowired
    public LessonConverter(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Lesson createLessonFromLessonDto(LessonDto lessonDto) {
        return new Lesson(lessonDto.getId(), lessonDto.getTitle(), lessonDto.getText(),
                courseRepository.getById(lessonDto.getCourseId()));
    }

    public LessonDto createLessonDtoFromLesson(Lesson lesson) {
        return new LessonDto(lesson.getId(), lesson.getTitle(), lesson.getText(),
                lesson.getCourse().getId());
    }

}
