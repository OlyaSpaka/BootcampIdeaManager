package com.example.demo.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 6, max = 30, message = "Username must have between 6 and 30 characters.")
    @NotEmpty(message = "Username field must be filled out.")
    @Pattern(regexp = "^[a-zA-Z0-9._\\-?!]+$", message = "No special characters, except for .?!-_")
    @Column(nullable = false, unique = true)
    private String username;


    @Email
    @NotEmpty(message = "Email field must be filled out.")
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min=6, message = "Password must have at least 6 characters.")
    @NotEmpty(message = "Password field must be filled out.")
    @Column(nullable = false)
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

}
