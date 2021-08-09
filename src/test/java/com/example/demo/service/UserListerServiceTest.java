package com.example.demo.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.converter.UserConverter;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.NotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class UserListerServiceTest {

  @InjectMocks
  UserListerServiceImpl userListerServiceImpl;

  @Mock
  UserRepository userRepository;

  @Mock
  UserConverter userConverter;

  @Mock
  Principal principal;

  static List<User> allUsers;

  static List<UserDto> allUsersDto;

  @BeforeAll
  public static void setUpWithUserList() {
    allUsers = new ArrayList<>();
    User userTestFirst = new User(1L, "Петр", "1234",
        new HashSet<Role>());
    User userTestSecond = new User(2L, "Веня", "12345",
        new HashSet<Role>());
    User userTestThird = new User(3L, "Анастасия", "12345",
        new HashSet<Role>());
    allUsers.add(userTestFirst);
    allUsers.add(userTestSecond);
    allUsers.add(userTestThird);
  }

  @BeforeAll
  public static void setUpWithUserDtoList() {
    allUsersDto = new ArrayList<>();
    UserDto userDtoTestFirst = new UserDto(1L, "Петр", "1234",
        new HashSet<Role>());
    UserDto userDtoTestSecond = new UserDto(2L, "Веня", "12345",
        new HashSet<Role>());
    UserDto userDtoTestThird = new UserDto(3L, "Анастасия", "12345",
        new HashSet<Role>());
    allUsersDto.add(userDtoTestFirst);
    allUsersDto.add(userDtoTestSecond);
    allUsersDto.add(userDtoTestThird);
  }

  @Test
  public void findAllUsersTest() {
    when(userRepository.findAll()).thenReturn(allUsers);
    List<User> allUsersForTest = userListerServiceImpl.findAllUsers();
    verify(userRepository, times(1)).findAll();
    Assertions.assertEquals(allUsers.size(), allUsersForTest.size());
  }

  @Test
  public void findAllUsersDtoTest() {
    when(userRepository.findAll()).thenReturn(allUsers);
    when(userConverter.createUserDtoListFromUserList(allUsers)).thenReturn(allUsersDto);
    List<UserDto> allUsersDtoForTest = userListerServiceImpl.findAllUsersDto();
    InOrder inOrder = Mockito.inOrder(userRepository, userConverter);
    inOrder.verify(userRepository, times(1)).findAll();
    inOrder.verify(userConverter, times(1)).createUserDtoListFromUserList(allUsers);
    Assertions.assertEquals(allUsers.size(), allUsersDtoForTest.size());
  }

  //?
  @Test
  public void findUserDtoByNotWrongIdTest() {
    User user = new User(1L, "Петр", "1234", new HashSet<>());
    UserDto userDto = new UserDto(1L, "Петр", "1234", new HashSet<>());
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userConverter.createUserDtoFromUser(user)).thenReturn(userDto);
    UserDto userDtoTest = userListerServiceImpl.findUserDtoById(1L);
    InOrder inOrder = Mockito.inOrder(userRepository, userConverter);
    inOrder.verify(userRepository, times(1)).findById(1L);
    inOrder.verify(userConverter, times(1)).createUserDtoFromUser(user);
    Assertions.assertEquals(1L, userDtoTest.getId());
    Assertions.assertEquals("Петр", userDtoTest.getUsername());
    Assertions.assertEquals("1234", userDtoTest.getPassword());
    Assertions.assertEquals(new HashSet<Role>(), userDto.getRoles());
  }

  @Test
  public void findUserDtoByWrongIdTest() {
    Assertions.assertThrows(NotFoundException.class, () -> {
      when(userRepository.findById(10L)).thenThrow(NotFoundException.class);
      UserDto userDtoTest = userListerServiceImpl.findUserDtoById(10L);
      verify(userRepository, times(1)).findById(10L);
    });
  }

  @Test
  public void findByCorrectUsernameTest() {
    User user = new User(1L, "Петр", "1234", new HashSet<>());
    UserDto userDto = new UserDto(1L, "Петр", "1234", new HashSet<>());
    when(userRepository.findUserByUsername("Петр")).thenReturn(Optional.of(user));
    when(userConverter.createUserDtoFromUser(user)).thenReturn(userDto);
    UserDto userDtoTest = userListerServiceImpl.findByUsername("Петр");
    InOrder inOrder = Mockito.inOrder(userRepository, userConverter);
    inOrder.verify(userRepository, times(1)).findUserByUsername("Петр");
    inOrder.verify(userConverter, times(1)).createUserDtoFromUser(user);
    Assertions.assertEquals(1L, userDtoTest.getId());
    Assertions.assertEquals("Петр", userDtoTest.getUsername());
    Assertions.assertEquals("1234", userDtoTest.getPassword());
    Assertions.assertEquals(new HashSet<Role>(), userDto.getRoles());
  }

  @Test
  public void findByWrongUsernameTest() {
    Assertions.assertThrows(NotFoundException.class, () -> {
      when(userRepository.findUserByUsername("Аристарх")).thenThrow(NotFoundException.class);
      UserDto userDtoTest = userListerServiceImpl.findByUsername("Аристарх");
      verify(userRepository, times(1)).findUserByUsername("Аристарх");
    });
  }

  @Test
  public void getUserByCorrectIdTest() {
    User user = new User(1L, "Петр", "1234", new HashSet<>());
    when(userRepository.getOne(1L)).thenReturn(user);
    User userTest = userListerServiceImpl.getUserById(1L);
    verify(userRepository, times(1)).getOne(1L);
    Assertions.assertEquals(1L, userTest.getId());
    Assertions.assertEquals("Петр", userTest.getUsername());
    Assertions.assertEquals("1234", userTest.getPassword());
    Assertions.assertEquals(new HashSet<Role>(), user.getRoles());
  }

  @Test
  public void getUserByWrongIdTest() {
    Assertions.assertThrows(NotFoundException.class, () -> {
      when(userRepository.getOne(10L)).thenThrow(NotFoundException.class);
      User userTest = userListerServiceImpl.getUserById(10L);
      verify(userRepository, times(1)).getOne(10L);
    });
  }

  @Test
  public void findUserByCorrectIdTest() {
    User user = new User(1L, "Петр", "1234", new HashSet<>());
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    User userTest = userListerServiceImpl.findUserById(1L);
    verify(userRepository, times(1)).findById(1L);
    Assertions.assertEquals(1L, userTest.getId());
    Assertions.assertEquals("Петр", userTest.getUsername());
    Assertions.assertEquals("1234", userTest.getPassword());
    Assertions.assertEquals(new HashSet<Role>(), user.getRoles());
  }

  @Test
  public void findUserByWrongIdTest() {
    Assertions.assertThrows(NotFoundException.class, () -> {
      when(userRepository.findById(10L)).thenThrow(NotFoundException.class);
      User userTest = userListerServiceImpl.findUserById(10L);
      verify(userRepository, times(1)).findById(10L);
    });
  }

  @Test
  public void deleteByIdWithNullCourseListTest() throws NotFoundException {
    User user = new User(1L, "Петр", "1234", new HashSet<>());
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    userListerServiceImpl.deleteById(1L);
    verify(userRepository, times(1)).deleteById(1L);
  }

  @Test
  public void deleteByIdWithNotNullCourseListTest() throws NotFoundException {
    User user = new User(1L, "Петр", "1234", new HashSet<>());
    Course newCourse = new Course();
    HashSet<User> newUserSet = new HashSet<>();
    newUserSet.add(user);
    newCourse.setUsers(newUserSet);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    userListerServiceImpl.deleteById(1L);
    verify(userRepository, times(1)).deleteById(1L);
  }

  @Test
  public void deleteByWrongIdTest() {
    Assertions.assertThrows(NotFoundException.class, () -> {
      when(userRepository.findById(10L)).thenThrow(NotFoundException.class);
      userListerServiceImpl.deleteById(10L);
      verify(userRepository, times(0)).deleteById(10L);
    });
  }

  @Test
  public void saveTest() {
    User user = new User(1L, "Петр", "1234", new HashSet<>());
    UserDto userDto = new UserDto(1L, "Петр", "1234", new HashSet<>());
    when(userConverter.createUserFromUserDto(userDto)).thenReturn(user);
    userListerServiceImpl.save(userDto);
    InOrder inOrder = Mockito.inOrder(userRepository, userConverter);
    inOrder.verify(userConverter, times(1)).createUserFromUserDto(userDto);
    inOrder.verify(userRepository, times(1)).save(user);
  }

  @Test
  public void recognizeActiveNotNullUserTest() {
    User user = new User(1L, "Петр", "1234", new HashSet<>());
    UserDto userDto = new UserDto(1L, "Петр", "1234", new HashSet<>());
    when(principal.getName()).thenReturn("Петр");
    when(userRepository.findUserByUsername("Петр")).thenReturn(Optional.of(user));
    when(userConverter.createUserDtoFromUser(user)).thenReturn(userDto);
    UserDto userDtoTest = userListerServiceImpl.recognizeActiveUser(principal);
    InOrder inOrder = Mockito.inOrder(principal, userRepository, userConverter);
    inOrder.verify(principal, times(1)).getName();
    inOrder.verify(userRepository, times(1)).findUserByUsername("Петр");
    inOrder.verify(userConverter, times(1)).createUserDtoFromUser(user);
    Assertions.assertEquals(1L, userDtoTest.getId());
    Assertions.assertEquals("Петр", userDtoTest.getUsername());
    Assertions.assertEquals("1234", userDtoTest.getPassword());
    Assertions.assertEquals(new HashSet<Role>(), user.getRoles());
  }

  @Test
  public void recognizeActiveNullUserTest() {
    Assertions.assertThrows(NotFoundException.class, () -> {
      when(principal.getName()).thenReturn(null);
      when(userRepository.findUserByUsername(null)).thenThrow(NotFoundException.class);
      UserDto userDtoTest = userListerServiceImpl.recognizeActiveUser(principal);
      verify(principal, times(1)).getName();
      verify(userRepository, times(1)).findUserByUsername(null);
    });
  }

  @Test
  public void findUsersNotAssignedToCourseTest() {
    when(userRepository.findUsersNotAssignedToCourse(1L)).thenReturn(allUsers);
    when(userConverter.createUserDtoListFromUserList(allUsers)).thenReturn(allUsersDto);
    List<UserDto> usersDtoListTest = userListerServiceImpl.findUsersNotAssignedToCourse(1L);
    InOrder inOrder = Mockito.inOrder(userRepository, userConverter);
    inOrder.verify(userRepository, times(1)).findUsersNotAssignedToCourse(1L);
    inOrder.verify(userConverter, times(1)).createUserDtoListFromUserList(allUsers);
    Assertions.assertEquals(allUsers.size(), usersDtoListTest.size());
  }
}
