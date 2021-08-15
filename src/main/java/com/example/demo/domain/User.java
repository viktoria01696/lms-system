package com.example.demo.domain;


import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @NotBlank(message = "Имя должно быть заполнено!")
  private String username;

  @ManyToMany(mappedBy = "users")
  private Set<Course> courses;

  @ManyToMany
  private Set<Role> roles;

  @Column
  private String password;

  @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
  private AvatarImage avatarImage;

  public User() {
  }

  public User(String username) {
    this.username = username;
  }

  public User(Long id, String username,String password, Set<Role> roles) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.roles = roles;
  }
  public User(String username,String password, Set<Role> roles) {
    this.username = username;
    this.password = password;
    this.roles = roles;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Set<Course> getCourses() {
    return courses;
  }

  public void setCourses(Set<Course> courses) {
    this.courses = courses;
  }

  public Set<Role> getRoles() { return roles; }

  public void setRoles(Set<Role> roles) { this.roles = roles; }

  public String getPassword() { return password; }

  public void setPassword(String password) { this.password = password; }

  public AvatarImage getAvatarImage() {
    return avatarImage;
  }

  public void setAvatarImage(AvatarImage avatarImage) {
    this.avatarImage = avatarImage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
