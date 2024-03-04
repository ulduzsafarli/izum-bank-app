package com.example.mybankapplication.model.customers;

import com.example.mybankapplication.annotations.AdultBirthDate;
import com.example.mybankapplication.model.PassportDto;
import com.example.mybankapplication.model.accounts.AccountRequest;
import com.example.mybankapplication.model.accounts.AccountResponse;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFilterDto {
    @Size(min = 3, max = 20, message = "Invalid firstname format")
    private String firstName;
    @Size(min = 3, max = 20, message = "Invalid lastname format")
    private String lastName;
    @AdultBirthDate
    private LocalDate birthDate;
}
