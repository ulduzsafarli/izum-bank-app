package org.matrix.izumbankapp.model.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest (
        @NotBlank(message = "Current password must not be blank")
        String currentPassword,
        @NotBlank(message = "New password must not be blank")
        @Size(min = 4, message = "New password must contain 4 characters")
        String newPassword,
        @NotBlank(message = "Confirmation password must not be blank")
        String confirmationPassword
){}
