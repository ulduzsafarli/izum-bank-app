package com.example.mybankapplication.model.users;

import com.example.mybankapplication.enumeration.auth.Role;
import com.example.mybankapplication.model.accounts.AccountResponse;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String email;

    private String password;

    private String phoneNumber;

    private String cif;

//    private List<AccountResponse> accounts;
}
