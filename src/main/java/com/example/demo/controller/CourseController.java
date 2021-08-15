package com.example.demo.controller;

import com.example.demo.domain.AvatarImage;
import com.example.demo.domain.Course;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.InternalServerError;
import com.example.demo.exception.NotFoundAvatarException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.AvatarStorageService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/course")
public class CourseController {

  private final CourseListerService courseListerService;
  private final UserListerService userListerService;
  private final LessonListerService lessonListerService;
  private final AvatarStorageService avatarStorageService;
  private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

  @Autowired
  public CourseController(CourseListerService courseListerService, UserListerService userListerService,
      LessonListerService lessonListerService, AvatarStorageService avatarStorageService) {
    this.courseListerService = courseListerService;
    this.userListerService = userListerService;
    this.lessonListerService = lessonListerService;
    this.avatarStorageService = avatarStorageService;
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
    model.addAttribute("avatarImage", courseListerService.findCourseById(id).getAvatarImage());
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
  public String courseNewForm(Model model) {
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
      UserDto user = userListerService.findUserDtoByUsername(request.getRemoteUser());
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

  @Secured("ROLE_ADMIN")
  @PostMapping("/{courseId}/avatar")
  public String updateAvatarImage(Model model, @PathVariable("courseId") Long courseId,
      @RequestParam("avatar") MultipartFile avatar) {
    if (!avatar.isEmpty()) {
      logger.info("File name {}, file content type {}, file size {}",
          avatar.getOriginalFilename(), avatar.getContentType(), avatar.getSize());
      try {
        avatarStorageService
            .saveCourseAvatar(courseId, avatar.getContentType(), avatar.getOriginalFilename(),
                avatar.getInputStream());
      } catch (Exception ex) {
        logger.info("", ex);
        throw new InternalServerError("Не удалось сохранить изображение");
      }
    }
    return String.format("redirect:/course/%d", courseId);
  }

  @GetMapping("/{courseId}/avatar")
  @ResponseBody
  public ResponseEntity<byte[]> avatarImage(@PathVariable("courseId") Long courseId) {
    AvatarImage avatarImage = courseListerService.findCourseById(courseId).getAvatarImage();
    if (avatarImage == null){
      byte[] data = avatarStorageService.getNullAvatarImage("course")
          .orElseThrow(NotFoundAvatarException::new);
      return ResponseEntity
          .ok()
          .contentType(MediaType.parseMediaType("image/jpeg"))
          .body(data);
    }
    else{
      String contentType = avatarStorageService.getContentTypeByCourse(courseId)
          .orElseThrow(NotFoundAvatarException::new);
      byte[] data = avatarStorageService.getAvatarImageByCourse(courseId)
          .orElseThrow(NotFoundAvatarException::new);
      return ResponseEntity
          .ok()
          .contentType(MediaType.parseMediaType(contentType))
          .body(data);}
  }

  @ExceptionHandler
  public ResponseEntity<Void> notFoundExceptionHandler(NotFoundAvatarException ex) {
    return ResponseEntity.notFound().build();
  }



}

