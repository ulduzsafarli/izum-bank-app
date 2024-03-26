package org.matrix.izumbankapp.model.users;

import lombok.Builder;
import org.matrix.izumbankapp.enumeration.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matrix.izumbankapp.model.accounts.AccountsUserResponse;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserAccountsResponse {

    private Long id;

    private String email;

    private String cif;

    private Role role;

    private List<AccountsUserResponse> accounts;

}
