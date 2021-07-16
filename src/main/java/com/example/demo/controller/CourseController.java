package com.example.demo.controller;

import com.example.demo.domain.Course;
import com.example.demo.service.CourseListerService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/course")
public class CourseController {

  private final CourseListerService courseListerService;

  @Autowired
  public CourseController(CourseListerService courseListerService) {
    this.courseListerService = courseListerService;
  }

  @GetMapping
  public String courseTable(Model model,
      @RequestParam(name = "titlePrefix", required = false) String titlePrefix) {
    model.addAttribute("activePage", "courses");
    model.addAttribute("courses", courseListerService.coursesByPrefix(titlePrefix));
    return "Courses";
  }

  @GetMapping("/{id}")
  public String courseForm(Model model, @PathVariable("id") Long id) {
    model.addAttribute("activePage", "courses");
    model.addAttribute("course", courseListerService.coursesById(id));
    return "CourseInformation";
  }

  @PostMapping
  public String submitCourseForm(@Valid Course course, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "CourseInformation";
    }
    courseListerService.saveCourse(course);
    return "redirect:/course";
  }

  @GetMapping("/new")
  public String courseForm(Model model) {
    model.addAttribute("activePage", "courses");
    model.addAttribute("course", courseListerService.createCourse());
    return "CourseInformation";
  }

  @DeleteMapping("/{id}")
  public String deleteCourse(@PathVariable("id") Long id) {
    courseListerService.deleteCourse(id);
    return "redirect:/course";
  }

}

