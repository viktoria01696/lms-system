package com.example.demo.service;

import com.example.demo.converter.UserConverter;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Course;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.NotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserListerServiceImpl implements UserListerService{

  private final UserRepository userRepository;
  private final UserConverter userConverter;

  @Autowired
  public UserListerServiceImpl(UserRepository userRepository,
      UserConverter userConverter){
    this.userRepository = userRepository;
    this.userConverter = userConverter;
  }

  @Override
  public List<User> findAllUsers(){ return userRepository.findAll();}

  @Override
  public List<UserDto> findAllUsersDto() {
    return userConverter.createUserDtoListFromUserList(userRepository.findAll());
  }

  @Override
  public UserDto findUserDtoById(long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(
            String.format("Пользователь с ID %d не найден!", id)
        ));
    return userConverter.createUserDtoFromUser(user);
  }

  @Override
  public UserDto findUserDtoByUsername(String username) {
    User user = userRepository.findUserByUsername(username)
        .orElseThrow(() -> new NotFoundException(
            String.format("Пользователь с логином %s не найден!", username)
        ));
    return userConverter.createUserDtoFromUser(user);
  }

  @Override
  public User findUserByUsername(String username) {
    User user = userRepository.findUserByUsername(username)
        .orElseThrow(() -> new NotFoundException(
            String.format("Пользователь с логином %s не найден!", username)
        ));
    return user;
  }

  @Override
  public User getUserById(Long id) {
    try { return userRepository.getOne(id);}
    catch (EntityNotFoundException ex){
      throw new NotFoundException(String.format("Пользователь с ID %d не найден!",id));}}

  @Override
  public User findUserById(Long id){return userRepository.findById(id)
      .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден!",id)));}

  @Override
  public void deleteById(long id) {
    User user = findUserById(id);
    Set<Course> coursesForUser = user.getCourses();
    if (coursesForUser != null){
      for (Course course: coursesForUser){
        course.getUsers().remove(user);
      }}
    userRepository.deleteById(id);
  }

  @Override
  public void save(UserDto userDto) {
    userRepository.save(userConverter.createUserFromUserDto(userDto));
  }

  @Override
  public UserDto recognizeActiveUser(Principal principal){
    User user = userRepository.findUserByUsername(principal.getName())
        .orElseThrow(() -> new NotFoundException(String.format("Пользователь с логином %s не найден!", principal.getName())
        ));
    return userConverter.createUserDtoFromUser(user);
  }

  @Override
  public List<UserDto> findUsersNotAssignedToCourse(Long id){
    return userConverter.createUserDtoListFromUserList(
        userRepository.findUsersNotAssignedToCourse(id));
  }
}

