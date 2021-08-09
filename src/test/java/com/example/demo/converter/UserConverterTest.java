package com.example.demo.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import java.util.HashSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserConverterTest {

  @InjectMocks
  UserConverter userConverter;

  @Mock
  UserRepository userRepository;

  @Mock
  PasswordEncoder passwordEncoder;

  Course course = new Course(1L, "Петр", "Джава для профессионалов");
  User user = new User(1L, "Петр", "1234",
      new HashSet<Role>());
  UserDto userDto = new UserDto(1L, "Петр", "1234",
      new HashSet<Role>());

  @Test
  void createUserFromUserDtoTest() {
    when(passwordEncoder.encode("1234")).thenReturn("1234");
    User userTest = userConverter.createUserFromUserDto(userDto);
    verify(passwordEncoder, times(1)).encode("1234");
    Assertions.assertEquals(1L, userTest.getId());
    Assertions.assertEquals("Петр", userTest.getUsername());
    Assertions.assertEquals("1234", userTest.getPassword());
    Assertions.assertEquals(new HashSet<Role>(), userTest.getRoles());
  }

  @Test
  void createUserDtoFromUserTest() {
    UserDto userDtoTest = userConverter.createUserDtoFromUser(user);
    Assertions.assertEquals(1L, userDtoTest.getId());
    Assertions.assertEquals("Петр", userDtoTest.getUsername());
    Assertions.assertEquals("", userDtoTest.getPassword());
    Assertions.assertEquals(new HashSet<Role>(), userDtoTest.getRoles());
  }

  @Test
  void createUserDtoListFromUserList() {
  }
}