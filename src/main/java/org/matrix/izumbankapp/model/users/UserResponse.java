package org.matrix.izumbankapp.model.users;

import org.matrix.izumbankapp.enumeration.auth.Role;
import org.matrix.izumbankapp.model.users.profile.UserProfileDto;
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
