package com.example.demo.security;

import com.example.demo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

  @Autowired
  public void authConfigure(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder,
      UserDetailsService userDetailsService) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("student")
        .password(passwordEncoder.encode("123"))
        .roles("STUDENT");
    auth.inMemoryAuthentication()
        .withUser("admin")
        .password(passwordEncoder.encode("123"))
        .roles("ADMIN");
    auth.userDetailsService(userDetailsService);
  }

  @Configuration
  public static class UiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
          .authorizeRequests()
          .antMatchers("/admin/**").hasRole("ADMIN")
          .antMatchers("/**").permitAll()
          .and()
          .formLogin()
          .defaultSuccessUrl("/course")
          .and()
          .exceptionHandling()
          .accessDeniedPage("/user/access_denied");
    }
  }
}
