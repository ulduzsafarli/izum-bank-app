package com.example.mybankapplication.model.auth;

import com.example.mybankapplication.annotations.AdultBirthDate;
import com.example.mybankapplication.enumeration.auth.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Firstname must not be null")
    @Size(min = 3, max = 20, message = "Invalid firstname format")
    private String firstName;

    @Size(min = 3, max = 20, message = "Invalid lastname format")
    @NotBlank(message = "Lastname must not be null")
    private String lastName;

    @AdultBirthDate
    private LocalDate birthDate;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be null")
    private String email;

    @NotBlank(message = "Phone number must not be null")
    @Pattern(regexp = "^0(?:50|51|55|70|77|10|60)\\d{7}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;

    private Set<Role> roles = Collections.singleton(Role.USER);
}
