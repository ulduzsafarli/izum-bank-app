package com.example.mybankapplication.model.users;

import com.example.mybankapplication.enumeration.auth.Role;
import com.example.mybankapplication.model.users.profile.UserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    private Long id;

    private String email;

    private String cif;

    private Role role;

    private UserProfileDto userProfile;
}
