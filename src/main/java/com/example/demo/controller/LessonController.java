package com.example.demo.controller;

import com.example.demo.conventer.LessonConverter;
import com.example.demo.dto.LessonDto;
import com.example.demo.service.LessonListerService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping("/lesson")
public class LessonController {

  private final LessonListerService lessonListerService;
  private final LessonConverter lessonConverter;

  @Autowired
  public LessonController(LessonListerService lessonListerService,
      LessonConverter lessonConverter) {
    this.lessonListerService = lessonListerService;
    this.lessonConverter = lessonConverter;
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/new")
  public String lessonForm(Model model, @RequestParam("course_id") long courseId) {
    model.addAttribute("courseId", courseId);
    model.addAttribute("lessonDto", new LessonDto(courseId));
    return "CreateLesson";
  }

  @Secured("ROLE_ADMIN")
  @PostMapping
  public String submitLessonForm(@Valid LessonDto lessonDto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "CreateLesson";
    }
    lessonListerService.saveLesson(lessonConverter.createLessonFromLessonDto(lessonDto));
    return String.format("redirect:/course/%d", lessonDto.getCourseId());
  }

  @GetMapping("/{id}")
  public String courseForm(Model model, @PathVariable("id") Long id) {
    model.addAttribute("lessonDto",
        lessonConverter.createLessonDtoFromLesson(lessonListerService.lessonById(id)));
    return "CreateLesson";
  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping("/{id}")
  public String deleteCourse(@PathVariable("id") Long id) {
    Long deletedId = lessonListerService.lessonById(id).getCourse().getId();
    lessonListerService.deleteLesson(id);
    return String.format("redirect:/course/%d", deletedId);
  }

}
