package com.example.demo.service;


import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dao.RoleRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.domain.Role;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleListerServiceTest {

  @InjectMocks
  RoleListerServiceImpl roleListerServiceImpl ;

  @Mock
  RoleRepository roleRepository;

  static List<Role> allRoles;

  @BeforeAll
  public static void setUpWithRoleList(){
    allRoles = new ArrayList<>();
    Role roleTestFirst = new Role("Студент");
    Role roleTestSecond = new Role("Администатор");
    Role roleTestThird = new Role("Наблюдатель");
    allRoles.add(roleTestFirst);
    allRoles.add(roleTestSecond);
    allRoles.add(roleTestThird);
  }

  @Test
  void findAllTest() {
    when(roleRepository.findAll()).thenReturn(allRoles);
    List<Role> allRolesTest = roleRepository.findAll();
    verify(roleRepository, times(1)).findAll();
    Assertions.assertEquals(allRoles.size(), allRolesTest.size());
  }
}