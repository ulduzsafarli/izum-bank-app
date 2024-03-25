package com.example.mybankapplication.model.users;

import com.example.mybankapplication.enumeration.auth.Role;
import com.example.mybankapplication.model.accounts.AccountResponse;
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
