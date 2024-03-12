package com.example.mybankapplication.model.users;

import com.example.mybankapplication.enumeration.auth.Role;
import com.example.mybankapplication.model.users.profile.UserProfileDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    private Long id;

    private String email;

    private String cif;

    private Role role;

    private UserProfileDto userProfile;

//    private List<AccountResponse> accounts;

}
