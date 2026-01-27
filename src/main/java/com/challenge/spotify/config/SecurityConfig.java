package com.challenge.spotify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(authorizeRequests ->
        authorizeRequests
          .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**").permitAll()
          .requestMatchers("/codechallenge/**").authenticated()
          .anyRequest().permitAll()
      )
      .csrf(csrf -> csrf.disable())
      .httpBasic(withDefaults());
    return http.build();
  }
}
