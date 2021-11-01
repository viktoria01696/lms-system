package com.example.demo.service;


import com.example.demo.dao.RoleRepository;
import com.example.demo.domain.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleListerServiceTest {

    static List<Role> allRoles;
    @InjectMocks
    RoleListerServiceImpl roleListerServiceImpl;
    @Mock
    RoleRepository roleRepository;

    @BeforeAll
    public static void setUpWithRoleList() {
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