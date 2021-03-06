package com.example.demo.controller;

import com.example.demo.converter.LessonConverter;
import com.example.demo.dto.LessonDto;
import com.example.demo.service.LessonListerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public String lessonNewForm(Model model, @RequestParam("course_id") long courseId) {
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
    public String lessonIdForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("lessonDto",
                lessonConverter.createLessonDtoFromLesson(lessonListerService.lessonById(id)));
        return "CreateLesson";
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public String deleteLesson(@PathVariable("id") Long id) {
        Long deletedId = lessonListerService.lessonById(id).getCourse().getId();
        lessonListerService.deleteLesson(id);
        return String.format("redirect:/course/%d", deletedId);
    }

}
