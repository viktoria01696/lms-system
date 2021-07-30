package com.example.demo.controller;

import com.example.demo.domain.Role;
import com.example.demo.dto.UserDto;
import com.example.demo.service.RoleListerService;
import com.example.demo.service.UserListerService;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/user")
public class UserController {

  private final RoleListerService roleListerService;
  private final UserListerService userListerService;

  @Autowired
  public UserController(RoleListerService roleListerService,
      UserListerService userListerService){
    this.roleListerService = roleListerService;
    this.userListerService = userListerService;
  }

  @ModelAttribute("roles")
  public List<Role> rolesAttribute() {
    return roleListerService.findAll();
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping
  public String userForm(Model model, Principal principal) {
    model.addAttribute("user", userListerService.recognizeActiveUser(principal));
    return "UserForm";
  }

  @PostMapping
  public String submitUserForm(@Valid @ModelAttribute("user") UserDto user,
      BindingResult bindingResult, HttpServletRequest request) {
    if (bindingResult.hasErrors()) {
      return "UserForm";
    }
    userListerService.save(user);
    if (request.isUserInRole("ROLE_ADMIN")) {
      return "redirect:/admin/users";
    }
    return "redirect:/course";
  }

  @GetMapping("/new")
  public String createNewUser(Model model) {
    model.addAttribute("user", new UserDto());
    return "UserForm";
  }

  @GetMapping("/access_denied")
  public ModelAndView accessDenied() {
    ModelAndView modelAndView = new ModelAndView("RoleRestrictions");
    modelAndView.setStatus(HttpStatus.I_AM_A_TEAPOT);
    return modelAndView;
  }

}
