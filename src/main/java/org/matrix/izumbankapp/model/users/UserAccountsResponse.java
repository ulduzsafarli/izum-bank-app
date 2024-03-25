package org.matrix.izumbankapp.model.users;

import org.matrix.izumbankapp.enumeration.auth.Role;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAccountsResponse {

    private Long id;

    private String email;

    private String cif;

    private Role role;

    private List<AccountResponse> accounts;

}
