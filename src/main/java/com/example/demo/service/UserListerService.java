package com.example.demo.service;

import com.example.demo.domain.User;
import java.util.List;

public interface UserListerService {

  List<User> getAllUsers();

  User getOneById(Long id);

  User findOneById(Long id);

}
