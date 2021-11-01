package com.example.demo.service;

import com.example.demo.dao.RoleRepository;
import com.example.demo.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleListerServiceImpl implements RoleListerService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleListerServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }


}
