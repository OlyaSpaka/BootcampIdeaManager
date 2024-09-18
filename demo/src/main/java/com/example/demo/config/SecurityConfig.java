package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection for testing purposes (enable in production)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").anonymous() // only unauthorized users can register/login
                        .requestMatchers("/css/auth-error-pages-style.css").permitAll() // if not added, guests cannot see css of login/register pages
                        .anyRequest().authenticated() // all other requests need to be authenticated
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // GET request for login page
                        .loginProcessingUrl("/auth/login") // POST request for login page
                        .failureUrl("/auth/login?error")
                        .defaultSuccessUrl("/ideas", true)
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutUrl("/auth/logout") // logout url that only accepts POST requests
                        .logoutSuccessUrl("/auth/login?logout") // on logout, redirect to this page
                        .permitAll()
                );
        return http.build();
    }

}
