package com.example.demo.service;

import com.example.demo.dao.LessonRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import com.example.demo.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonListerServiceTest {

    static List<Lesson> allLessons;
    static List<LessonDto> allLessonsDto;
    static Course course;
    @InjectMocks
    LessonListerServiceImpl lessonListerServiceImpl;
    @Mock
    LessonRepository lessonRepository;

    @BeforeAll
    public static void setUpWithLessonList() {
        allLessons = new ArrayList<>();
        course = new Course(1L, "Петр", "Джава для профессионалов");
        Lesson lessonTestFirst = new Lesson(1L, "Первый урок", "Описание", course);
        Lesson lessonTestSecond = new Lesson(2L, "Второй урок", "Описание", course);
        Lesson lessonTestThird = new Lesson(3L, "Третий урок", "Описание", course);
        allLessons.add(lessonTestFirst);
        allLessons.add(lessonTestSecond);
        allLessons.add(lessonTestThird);
    }

    @BeforeAll
    public static void setUpWithLessonDtoList() {
        allLessonsDto = new ArrayList<>();
        course = new Course(1L, "Петр", "Джава для профессионалов");
        LessonDto lessonTestFirst = new LessonDto(1L, "Первый урок", "Описание", 1L);
        LessonDto lessonTestSecond = new LessonDto(2L, "Второй урок", "Описание", 1L);
        LessonDto lessonTestThird = new LessonDto(3L, "Третий урок", "Описание", 1L);
        allLessonsDto.add(lessonTestFirst);
        allLessonsDto.add(lessonTestSecond);
        allLessonsDto.add(lessonTestThird);
    }

    @Test
    void saveLessonTest() {
        lessonListerServiceImpl.saveLesson(allLessons.get(0));
        verify(lessonRepository, times(1)).save(allLessons.get(0));
    }

    @Test
    void lessonByCorrectIdTest() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(allLessons.get(0)));
        Lesson lessonTest = lessonListerServiceImpl.lessonById(1L);
        verify(lessonRepository, times(1)).findById(1L);
        Assertions.assertEquals(1L, lessonTest.getId());
        Assertions.assertEquals("Первый урок", lessonTest.getTitle());
        Assertions.assertEquals("Описание", lessonTest.getText());
        Assertions.assertEquals(course, lessonTest.getCourse());
    }

    @Test
    void lessonByWrongIdTest() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            when(lessonRepository.findById(10L)).thenThrow(NotFoundException.class);
            Lesson lessonTest = lessonListerServiceImpl.lessonById(10L);
            verify(lessonRepository, times(1)).findById(10L);
        });
    }

    @Test
    void deleteLessonByCorrectIdTest() {
        lessonListerServiceImpl.deleteLesson(1L);
        verify(lessonRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteLessonByWrongIdTest() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            doThrow(NotFoundException.class).when(lessonRepository).deleteById(10L);
            lessonListerServiceImpl.deleteLesson(10L);
            verify(lessonRepository, times(1)).deleteById(10L);
        });
    }

    @Test
    void getLessonsDtoWithCorrectCourseIdTest() {
        when(lessonRepository.findAllForLessonIdWithoutText(1L)).thenReturn(allLessonsDto);
        List<LessonDto> lessonsDtoTest = lessonListerServiceImpl.getLessonsDto(1L);
        verify(lessonRepository, times(1)).findAllForLessonIdWithoutText(1L);
        Assertions.assertEquals(allLessonsDto.size(), lessonsDtoTest.size());
    }

    @Test
    void getLessonsDtoWithWrongCourseIdTest() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            when(lessonRepository.findAllForLessonIdWithoutText(10L)).thenThrow(NotFoundException.class);
            List<LessonDto> lessonsDtoTest = lessonListerServiceImpl.getLessonsDto(10L);
            verify(lessonRepository, times(1)).findAllForLessonIdWithoutText(10L);
        });
    }
}