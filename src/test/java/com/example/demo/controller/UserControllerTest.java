package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.example.demo.domain.Role;
import com.example.demo.dto.UserDto;
import com.example.demo.service.CourseListerService;
import com.example.demo.service.LessonListerService;
import com.example.demo.service.RoleListerService;
import com.example.demo.service.UserListerService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  UserController userController;

  @MockBean
  RoleListerService roleListerService;

  @MockBean
  UserListerService userListerService;

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
  void rolesAttributeTest() {
    ArrayList<Role> roles = new ArrayList<>();
    roles.add(new Role());
    roles.add(new Role());
    when(roleListerService.findAll()).thenReturn(roles);
    List<Role> rolesTest = roleListerService.findAll();
    verify(roleListerService, times(1)).findAll();
    assertEquals(roles.size(), rolesTest.size());
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void submitUserFormTest() throws Exception{
    MockHttpServletRequestBuilder builder = post("/user")
        .with(csrf());
    mockMvc.perform(builder)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("UserForm"));
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
  void createNewUserTest() throws Exception{
    MockHttpServletRequestBuilder builder = get("/user/new");
    mockMvc.perform(builder)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.model().size(2))
        .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("roles"))
        .andExpect(MockMvcResultMatchers.view().name("UserForm"));
  }

  @Test
  void accessDeniedTest() throws Exception{
    MockHttpServletRequestBuilder builder = get("/user/access_denied");
    mockMvc.perform(builder)
        .andExpect(status().is(418))
        .andExpect(MockMvcResultMatchers.view().name("RoleRestrictions"));
  }
}