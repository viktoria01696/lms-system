package com.example.demo.service;

import com.example.demo.dao.CourseRepository;
import com.example.demo.domain.Course;
import com.example.demo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


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
        return courseRepository.findByTitleLike(prefix == null ? "" + "%" : prefix + "%");
    }

    @Override
    public Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @Override
    public void saveCourse(Course course) {
        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public Course getOneById(Long id) {
        try {
            return courseRepository.getOne(id);
        } catch (EntityNotFoundException ex) {
            throw new NotFoundException(String.format("Курс с ID %d не найден!", id));
        }
    }
}
