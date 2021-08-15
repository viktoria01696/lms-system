package com.example.demo.controller;

import com.example.demo.domain.AvatarImage;
import com.example.demo.domain.Role;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.InternalServerError;
import com.example.demo.exception.NotFoundAvatarException;
import com.example.demo.service.AvatarStorageService;
import com.example.demo.service.RoleListerService;
import com.example.demo.service.UserListerService;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/user")
public class UserController {

  private final RoleListerService roleListerService;
  private final UserListerService userListerService;
  private final AvatarStorageService avatarStorageService;
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  public UserController(RoleListerService roleListerService,
      UserListerService userListerService, AvatarStorageService avatarStorageService) {
    this.roleListerService = roleListerService;
    this.userListerService = userListerService;
    this.avatarStorageService = avatarStorageService;
  }

  @ModelAttribute("roles")
  public List<Role> rolesAttribute() {
    return roleListerService.findAll();
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping
  public String userForm(Model model, Principal principal) {
    model.addAttribute("user", userListerService.recognizeActiveUser(principal));
    model.addAttribute("username", userListerService.recognizeActiveUser(principal).getUsername());
    model.addAttribute("avatarImage", userListerService.findUserByUsername(principal.getName())
        .getAvatarImage());
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

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/avatar")
  public String updateAvatarImage(Model model, Authentication auth,
      @RequestParam("avatar") MultipartFile avatar) {
    if (!avatar.isEmpty()) {
      logger.info("File name {}, file content type {}, file size {}",
          avatar.getOriginalFilename(), avatar.getContentType(), avatar.getSize());
      try {
        avatarStorageService
            .saveUserAvatar(auth.getName(), avatar.getContentType(), avatar.getOriginalFilename(),
                avatar.getInputStream());
      } catch (Exception ex) {
        logger.info("", ex);
        throw new InternalServerError("Не удалось сохранить изображение");
      }
    }
    return "redirect:/user";
  }

  @GetMapping("/avatar")
  @ResponseBody
  public ResponseEntity<byte[]> avatarImage(Authentication auth) {
    AvatarImage avatarImage = userListerService.findUserByUsername(auth.getName()).getAvatarImage();
    if (avatarImage == null) {
      byte[] data = avatarStorageService.getNullAvatarImage("user")
          .orElseThrow(NotFoundAvatarException::new);
      return ResponseEntity
          .ok()
          .contentType(MediaType.parseMediaType("image/jpeg"))
          .body(data);
    } else {
      String contentType = avatarStorageService.getContentTypeByUser(auth.getName())
          .orElseThrow(NotFoundAvatarException::new);
      byte[] data = avatarStorageService.getAvatarImageByUser(auth.getName())
          .orElseThrow(NotFoundAvatarException::new);
      return ResponseEntity
          .ok()
          .contentType(MediaType.parseMediaType(contentType))
          .body(data);
    }
  }

  @ExceptionHandler
  public ResponseEntity<Void> notFoundExceptionHandler(NotFoundAvatarException ex) {
    return ResponseEntity.notFound().build();
  }

}
