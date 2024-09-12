package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
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
                        .requestMatchers("/auth/**").anonymous() // Permit access to /auth for logged out users
                        .requestMatchers("/css/style.css").permitAll()
                        .anyRequest().authenticated() // All other requests need to be authenticated
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // Custom login page
                        .loginProcessingUrl("/auth/login") // URL to submit login credentials
                        .failureUrl("/auth/login?error")
                        .defaultSuccessUrl("/home", true) // Redirect to /home on success
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true) // Invalidate session on logout
                        .clearAuthentication(true) // Clear authentication on logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout")) // Set the logout URL
                        .logoutSuccessUrl("/login?logout") // Redirect to /login?logout on logout
                        .permitAll()
                );


        return http.build();
    }

}
