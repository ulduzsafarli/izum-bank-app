package com.example.mybankapplication.model.users;

import com.example.mybankapplication.annotations.AdultBirthDate;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterDto {
    @Size(min = 3, max = 20, message = "Invalid firstname format")
    private String firstName;
    @Size(min = 3, max = 20, message = "Invalid lastname format")
    private String lastName;
    @AdultBirthDate
    private LocalDate birthDate;
}
