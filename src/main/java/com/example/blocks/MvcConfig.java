package com.example.blocks;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/home").setViewName("home");
    registry.addViewController("/").setViewName("home");
    registry.addViewController("/start").setViewName("start");
    registry.addViewController("/play").setViewName("play");
    registry.addViewController("/kouho").setViewName("kouho");
    registry.addViewController("/login").setViewName("login");
  }


  /**
   * Reactのローカル開発環境からアクセスを許可するための設定
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("api/**")
      .allowedOrigins("http://localhost:3000")
      .allowedMethods("GET", "POST", "PUT", "DELETE");
  }

}
