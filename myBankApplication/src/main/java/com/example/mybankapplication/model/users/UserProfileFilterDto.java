package com.example.mybankapplication.model.users;

import com.example.mybankapplication.annotations.AdultBirthDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class UserProfileFilterDto {
    @Size(min = 3, max = 20, message = "Invalid firstname format")
    private String firstName;
    @Size(min = 3, max = 20, message = "Invalid lastname format")
    private String lastName;
    @AdultBirthDate
    private LocalDate birthDate;
    @NotBlank(message = "Phone number must not be null")
    @Pattern(regexp = "^0(?:50|51|55|70|77|10|60)\\d{7}$", message = "Invalid phone number format")
    private String phoneNumber;
}
