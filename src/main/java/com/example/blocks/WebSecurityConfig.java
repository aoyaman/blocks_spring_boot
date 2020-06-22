package com.example.blocks;

import com.example.blocks.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AccountService userService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
      .authorizeRequests()
        // .antMatchers("/", "/start", "/css/**", "/js/**", "/img/**")
        // .permitAll().anyRequest().authenticated()
        .antMatchers("/", "/signup", "/login", "/login-error", "/logout", "/css/**", "/js/**",
            "/image/**").permitAll()
        .antMatchers("/**").hasRole("USER")
        .and()
      .formLogin()
        .loginPage("/login").failureUrl("/login-error")
        .permitAll()
        .and()
      .logout().permitAll();
  }

  // ロード時に、「admin」ユーザを登録する。
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    // TODO: propertyでadmin情報は管理しましょう。
    userService.registerAdmin("admin", "secret", "admin@localhost");
  }

  // PasswordEncoder(BCryptPasswordEncoder)メソッド
  @Bean
  public PasswordEncoder passwordEncoder() {
    //
    return new BCryptPasswordEncoder();
  }

  // @Bean
  // @Override
  // public UserDetailsService userDetailsService() {
  //   UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build();

  //   return new InMemoryUserDetailsManager(user);
  // }
}
/*
public class WebSecurityConfig {

}
*/