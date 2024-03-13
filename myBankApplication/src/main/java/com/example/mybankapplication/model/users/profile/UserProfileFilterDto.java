package com.example.mybankapplication.model.users.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileFilterDto {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phoneNumber;
}
