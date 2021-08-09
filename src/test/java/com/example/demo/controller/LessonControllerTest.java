package com.example.demo.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.converter.LessonConverter;
import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.dto.LessonDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.LessonListerService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@WebMvcTest(LessonController.class)
class LessonControllerTest {

  @Autowired
  LessonController lessonController;

  @MockBean
  LessonListerService lessonListerService;

  @MockBean
  LessonConverter lessonConverter;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .defaultRequest(get("/")
        .with(user("user").password("password").roles("ADMIN")))
        .apply(springSecurity())
        .build();
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void lessonNewFormTest() throws Exception{
    MockHttpServletRequestBuilder builder = get("/lesson/new")
        .param("course_id", "1L");
    mockMvc
        .perform(builder)
        .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void submitLessonFormTest() throws Exception{
    MockHttpServletRequestBuilder builder = post("/lesson").with(csrf());
    mockMvc.perform(builder)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("CreateLesson"));
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void lessonCorrectIdFormTest() throws Exception{
    Lesson lesson = new Lesson();
    when(lessonListerService.lessonById(1L)).thenReturn(lesson);
    when(lessonConverter.createLessonDtoFromLesson(lesson)).thenReturn(new LessonDto());
    MockHttpServletRequestBuilder builder = get("/lesson/{id}", 1L);
    mockMvc
        .perform(builder)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.model().size(1))
        .andExpect(MockMvcResultMatchers.model().attributeExists("lessonDto"))
        .andExpect(MockMvcResultMatchers.view().name("CreateLesson"));
    InOrder inOrder = Mockito.inOrder(lessonListerService, lessonConverter);
    inOrder.verify(lessonListerService,times(1)).lessonById(1L);
    inOrder.verify(lessonConverter, times(1)).createLessonDtoFromLesson(lesson);
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void lessonWrongIdFormTest() throws Exception{
    Lesson lesson = new Lesson();
    when(lessonListerService.lessonById(1L)).thenThrow(new NotFoundException("Запрашиваемый урок не найден в базе!"));
    MockHttpServletRequestBuilder builder = get("/lesson/{id}", 1L);
    mockMvc
        .perform(builder)
        .andExpect(status().isNotFound())
        .andExpect(MockMvcResultMatchers.model().size(1))
        .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
        .andExpect(MockMvcResultMatchers.view().name("SomethingGoesWrong"));
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void deleteCourseTest() throws Exception{
    Course course = new Course(1L, "Петр", "Джава для профессионалов");
    Lesson lesson = new Lesson(1L, "Первый урок", "Описание", course);
    when(lessonListerService.lessonById(1L)).thenReturn(lesson);
    doNothing().when(lessonListerService).deleteLesson(1L);
    MockHttpServletRequestBuilder builder = delete("/lesson/{id}", 1L).with(csrf());
    mockMvc
        .perform(builder)
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/course/1"));
    InOrder inOrder = Mockito.inOrder(lessonListerService);
    inOrder.verify(lessonListerService, times(1)).lessonById(1L);
    inOrder.verify(lessonListerService,times(1)).deleteLesson(1L);
  }
}