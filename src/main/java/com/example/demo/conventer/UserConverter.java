package com.example.demo.conventer;

import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.domain.Lesson;
import com.example.demo.domain.User;
import com.example.demo.dto.LessonDto;
import com.example.demo.dto.UserDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserConverter(UserRepository userRepository,
      PasswordEncoder passwordEncoder){
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User createUserFromUserDto(UserDto userDto){
    return new User(userDto.getId(), userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()),
        userDto.getRoles());
  }

  public UserDto createUserDtoFromUser(User user){
    return new UserDto(user.getId(), user.getUsername(), "",
        user.getRoles());
  }

  public List<UserDto> createUserDtoListFromUserList(List<User> users){
    return users.stream()
        .map(usr -> new UserDto(usr.getId(), usr.getUsername(), "", usr.getRoles()))
        .collect(Collectors.toList());
  }

}
