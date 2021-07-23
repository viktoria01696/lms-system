package com.example.demo.dao;

import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

  @Query("select new com.example.demo.dto.LessonDto(l.id, l.title, l.text, l.course.id) " +
      "from Lesson l")
  List<LessonDto> findAllWithProjection();

  @Query("select new com.example.demo.dto.LessonDto(l.id, l.title, l.text, l.course.id) " +
      "from Lesson l where l.course.id = :id")
  List<LessonDto> findAllForLessonIdWithoutText(@Param("id") long id);

}
