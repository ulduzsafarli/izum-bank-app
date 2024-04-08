package org.matrix.izumbankapp.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
        @NotBlank(message = "Email address is required")
        @Email(message = "Invalid email address format")
        String email,
        @NotBlank(message = "Password is required")
        @Size(min = 4, message = "Password must be at least 4 characters long")
        String password
) {}
