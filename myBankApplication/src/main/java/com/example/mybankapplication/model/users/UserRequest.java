package com.example.mybankapplication.model.users;

import com.example.mybankapplication.enumeration.auth.Role;
import com.example.mybankapplication.model.users.profile.UserProfileDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @Email(message = "Invalid email format", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @NotBlank(message = "Email must not be null")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;

    @Pattern(regexp = "\\d{5}", message = "CIF must be a 5-digit number")
    private String cif;

    private Set<Role> roles = Collections.singleton(Role.USER);

    private UserProfileDto userProfile;


//    private List<AccountRequest> accounts;
}