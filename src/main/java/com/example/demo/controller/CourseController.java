package com.example.demo.controller;

import com.example.demo.domain.Course;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import com.example.demo.service.CourseListerService;
import com.example.demo.service.LessonListerService;
import com.example.demo.service.UserListerService;
import java.security.Principal;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
  private final UserListerService userListerService;
  private final LessonListerService lessonListerService;
  private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

  @Autowired
  public CourseController(CourseListerService courseListerService, UserListerService userListerService,
      LessonListerService lessonListerService) {
    this.courseListerService = courseListerService;
    this.userListerService = userListerService;
    this.lessonListerService = lessonListerService;
  }

  @GetMapping
  public String courseTable(Model model, Principal principal,
      @RequestParam(name = "titlePrefix", required = false) String titlePrefix) {
    if (principal != null) {
      logger.info("Request from user '{}'", principal.getName());
    }
    model.addAttribute("activePage", "courses");
    model.addAttribute("courses", courseListerService.coursesByPrefix(titlePrefix));
    return "Courses";
  }

  @GetMapping("/{id}")
  public String courseForm(Model model, @PathVariable("id") Long id) {
    model.addAttribute("activePage", "courses");
    model.addAttribute("course", courseListerService.findCourseById(id));
    model.addAttribute("lessons", lessonListerService.getLessonsDto(id));
    model.addAttribute("users", courseListerService.findCourseById(id).getUsers());
    return "CourseInformation";
  }

  @Secured("ROLE_ADMIN")
  @PostMapping
  public String submitCourseForm(@Valid Course course, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "CourseInformation";
    }
    courseListerService.saveCourse(course);
    return "redirect:/course";
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/new")
  public String courseForm(Model model) {
    model.addAttribute("activePage", "courses");
    model.addAttribute("course", new Course());
    return "CourseInformation";
  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping("/{id}")
  public String deleteCourse(@PathVariable("id") Long id) {
    courseListerService.deleteCourse(id);
    return "redirect:/course";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{id}/assign")
  public String assignCourse(Model model, HttpServletRequest request,
      @PathVariable("id") Long id) {
    model.addAttribute("courseId", id);
    if (request.isUserInRole("ROLE_ADMIN")) {
      model.addAttribute("users", userListerService.findUsersNotAssignedToCourse(id));
    }
    else{
      UserDto user = userListerService.findByUsername(request.getRemoteUser());
      model.addAttribute("users", Collections.singletonList(user));
    }
    return "AssignCourse";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{courseId}/assign")
  public String assignUserForm(@PathVariable("courseId") Long courseId,
      @RequestParam("userId") Long id) {
    User user = userListerService.getUserById(id);
    Course course = courseListerService.getOneById(courseId);
    course.getUsers().add(user);
    user.getCourses().add(course);
    courseListerService.saveCourse(course);
    return String.format("redirect:/course/%d", courseId);
  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping("/{courseId}/user/{id}")
  public String deleteUser(@PathVariable("id") Long userId,
      @PathVariable("courseId") Long courseId) {
    User user = userListerService.findUserById(userId);
    Course course = courseListerService.findCourseById(courseId);
    user.getCourses().remove(course);
    course.getUsers().remove(user);
    courseListerService.saveCourse(course);
    return String.format("redirect:/course/%d", courseId);
  }


}

