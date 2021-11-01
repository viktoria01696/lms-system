package com.example.demo.controller;

import com.example.demo.domain.Role;
import com.example.demo.service.RoleListerService;
import com.example.demo.service.UserListerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserListerService userListerService;
    private final RoleListerService roleListerService;

    public AdminController(UserListerService userListerService,
                           RoleListerService roleListerService) {
        this.userListerService = userListerService;
        this.roleListerService = roleListerService;
    }

    @ModelAttribute("roles")
    public List<Role> rolesAttribute() {
        return roleListerService.findAll();
    }

    @GetMapping("/users")
    public String userForm(Model model) {
        model.addAttribute("users", userListerService.findAllUsersDto());
        return "Users";
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userListerService.deleteById(id);
        return "redirect:/admin/users";
    }

}
