package com.example.mybankapplication.model.users;

import com.example.mybankapplication.model.PassportDto;
import com.example.mybankapplication.model.accounts.AccountResponse;
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
    //    private String cif;
    private String phoneNumber;
//    private PassportDto passport;
    private List<AccountResponse> accounts;
}
