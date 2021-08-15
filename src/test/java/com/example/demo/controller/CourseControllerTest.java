package com.example.demo.controller;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.example.demo.domain.Course;
import com.example.demo.domain.User;
import com.example.demo.dto.LessonDto;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.CourseExceptionHandler;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.CourseListerService;
import com.example.demo.service.LessonListerService;
import com.example.demo.service.UserListerService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

  @Autowired
  CourseController courseController;

  @MockBean
  CourseListerService courseListerService;

  @MockBean
  UserListerService userListerService;

  @MockBean
  LessonListerService lessonListerService;

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
  void courseTableTest() throws Exception{
    String title = "Джава";
    when(courseListerService.coursesByPrefix(title))
        .thenReturn(Collections.singletonList(new Course()));
    MockHttpServletRequestBuilder builder = get("/course")
        .param("titlePrefix", title);
    MockMvcBuilders.standaloneSetup(courseController)
        .build()
        .perform(builder)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.model().size(2))
        .andExpect(MockMvcResultMatchers.model().attributeExists("activePage"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("courses"))
        .andExpect(MockMvcResultMatchers.view().name("Courses"));
    verify(courseListerService, times(1)).coursesByPrefix(title);
  }

  @Test
  void courseFormWithCorrectIdTest() throws Exception{
    Course course = new Course(1L,"Аркадий","Джава");
    course.setUsers(new HashSet<>());
    when(courseListerService.findCourseById(1L))
        .thenReturn(course);
    when(lessonListerService.getLessonsDto(1L))
        .thenReturn(Collections.singletonList(new LessonDto()));
    MockHttpServletRequestBuilder builder = get("/course/{id}",1L);
    MockMvcBuilders.standaloneSetup(courseController)
        .build()
        .perform(builder)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.model().size(4))
        .andExpect(MockMvcResultMatchers.model().attributeExists("activePage"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("course"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("lessons"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
        .andExpect(MockMvcResultMatchers.view().name("CourseInformation"));
    InOrder inOrder = Mockito.inOrder(courseListerService, lessonListerService);
    inOrder.verify(courseListerService, times(1)).findCourseById(1L);
    inOrder.verify(lessonListerService, times(1)).getLessonsDto(1L);
    inOrder.verify(courseListerService, times(1)).findCourseById(1L);
  }

  @Test
  void courseFormWithWrongIdTest() throws Exception{
    when(courseListerService.findCourseById(10L)).thenThrow(new NotFoundException(1L));
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
        .get("/course/{id}",10L);
    MockMvcBuilders.standaloneSetup(courseController)
        .setControllerAdvice(new CourseExceptionHandler())
        .build()
        .perform(builder)
        .andExpect(status().isNotFound())
        .andExpect(MockMvcResultMatchers.model().size(1))
        .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
        .andExpect(MockMvcResultMatchers.view().name("SomethingGoesWrong"));
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void submitCourseFormTest() throws Exception{
    MockHttpServletRequestBuilder builder = post("/course")
        .with(csrf());
    mockMvc.perform(builder)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("CourseInformation"));
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void courseNewFormTest() throws Exception {
    MockHttpServletRequestBuilder builder = get("/course/new");
    mockMvc.perform(builder)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.model().size(2))
        .andExpect(MockMvcResultMatchers.model().attributeExists("activePage"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("course"))
        .andExpect(MockMvcResultMatchers.view().name("CourseInformation"));
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void deleteCourseTest() throws Exception{
    MockHttpServletRequestBuilder builder = delete("/course/{id}", 1).with(csrf());
    mockMvc.perform(builder)
        .andExpect(status().isFound())
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/course"));
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void assignCourseWithAdminTest() throws Exception{
    ArrayList<UserDto> usersNotAssign = new ArrayList<>();
    usersNotAssign.add(new UserDto());
    usersNotAssign.add(new UserDto());
    usersNotAssign.add(new UserDto());
    when(userListerService.findUsersNotAssignedToCourse(1L)).thenReturn(usersNotAssign);
    MockHttpServletRequestBuilder builder = get("/course/{id}/assign", 1);
    mockMvc.perform(builder)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.model().size(2))
        .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("courseId"))
        .andExpect(MockMvcResultMatchers.view().name("AssignCourse"));
    verify(userListerService, times(1)).findUsersNotAssignedToCourse(1L);
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void assignCourseWithStudentTest() throws Exception{
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .defaultRequest(get("/")
        .with(user("user").password("password").roles("STUDENT")))
        .apply(springSecurity())
        .build();
    when(userListerService.findUserDtoByUsername("user")).thenReturn(new UserDto());
    MockHttpServletRequestBuilder builder = get("/course/{id}/assign", 1);
    mockMvc
        .perform(builder)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.model().size(2))
        .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("courseId"))
        .andExpect(MockMvcResultMatchers.view().name("AssignCourse"));
    verify(userListerService, times(1)).findUserDtoByUsername("user");
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void assignUserFormTest() throws Exception {
    Course courseTest = new Course(1l, "Петр", "Джава для профессионалов");
    courseTest.setUsers(new HashSet<>());
    User userTest = new User(1L,"Виктор", "1234", new HashSet<>());
    userTest.setCourses(new HashSet<>());
    when(userListerService.getUserById(1L)).thenReturn(userTest);
    when(courseListerService.getOneById(1L)).thenReturn(courseTest);
    MockHttpServletRequestBuilder builder = post("/course/{courseId}/assign", 1L)
        .param("userId", "1")
        .with(csrf());
    mockMvc
        .perform(builder)
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/course/1"));
    InOrder inOrder = Mockito.inOrder(userListerService, courseListerService);
    inOrder.verify(userListerService, times(1)).getUserById(1L);
    inOrder.verify(courseListerService, times(1)).getOneById(1L);
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void deleteUserTest() throws Exception{
    Course courseTest = new Course(1L, "Петр", "Джава для профессионалов");
    User userTest = new User(1L,"Виктор", "1234", new HashSet<>());
    courseTest.setUsers(new HashSet<>());
    userTest.setCourses(new HashSet<>());
    when(userListerService.findUserById(1L)).thenReturn(userTest);
    when(courseListerService.findCourseById(1L)).thenReturn(courseTest);
    MockHttpServletRequestBuilder builder = delete("/course/{courseId}/user/{id}", 1L, 1L)
        .with(csrf());
    mockMvc
        .perform(builder)
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/course/1"));
  }
}