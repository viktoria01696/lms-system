package com.example.demo.controller;

import com.example.demo.domain.Role;
import com.example.demo.service.RoleListerService;
import com.example.demo.service.UserListerService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    AdminController adminController;

    @MockBean
    UserListerService userListerService;

    @MockBean
    RoleListerService roleListerService;

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
    void rolesAttribute() {
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
    void userForm() throws Exception {
        when(userListerService.findAllUsersDto()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder builder = get("/admin/users");
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("roles"))
                .andExpect(MockMvcResultMatchers.view().name("Users"));
        verify(userListerService, times(1)).findAllUsersDto();
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"ADMIN"})
    void deleteUser() throws Exception {
        doNothing().when(userListerService).deleteById(1L);
        MockHttpServletRequestBuilder builder = delete("/admin/user/{id}", 1L)
                .with(csrf());
        mockMvc.perform(builder)
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/users"));
    }
}