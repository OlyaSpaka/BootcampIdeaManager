package com.example.demo.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputUserDTO { //for creation of new users
    @Size(min = 6, max = 30, message = "Username must have between 6 and 30 characters.")
    @NotEmpty(message = "Username field must be filled out.")
    @Pattern(regexp = "^[a-zA-Z0-9._\\-?!]+$", message = "No special characters, except for .?!-_")
    String username;

    @Email
    @NotEmpty(message = "Email field must be filled out.")
    String email;

    @Size(min=6, message = "Password must have at least 6 characters.")
    @NotEmpty(message = "Password field must be filled out.")
    String password;

    public InputUserDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public InputUserDTO() {
    }
}
