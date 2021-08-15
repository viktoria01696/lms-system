package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import java.security.Principal;
import java.util.List;

public interface UserListerService {

  List<User> findAllUsers();

  List<UserDto> findAllUsersDto();

  UserDto findUserDtoById(long id);

  UserDto findUserDtoByUsername(String username);

  User findUserByUsername(String username);

  User getUserById(Long id);

  User findUserById(Long id);

  void deleteById(long id);

  void save(UserDto userDto);

  UserDto recognizeActiveUser(Principal principal);

  public List<UserDto> findUsersNotAssignedToCourse(Long id);

}
