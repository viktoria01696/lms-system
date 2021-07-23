package com.example.demo.service;

import com.example.demo.dao.UserRepository;
import com.example.demo.domain.User;
import com.example.demo.exception.NotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserListerServiceImpl implements UserListerService{

  private UserRepository userRepository;

  @Autowired
  public UserListerServiceImpl(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  @Override
  public List<User> getAllUsers(){ return userRepository.findAll();}

  @Override
  public User getOneById(Long id){return userRepository.getOne(id);}

  @Override
  public User findOneById(Long id){return userRepository.findById(id)
      .orElseThrow(() -> new NotFoundException(id));}
}
