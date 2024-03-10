package com.example.mybankapplication.model.users;

import com.example.mybankapplication.annotations.AdultBirthDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "Firstname must not be null")
    @Size(min = 3, max = 20, message = "Invalid firstname format")
    private String firstName;

    @Size(min = 3, max = 20, message = "Invalid lastname format")
    @NotBlank(message = "Lastname must not be null")
    private String lastName;

    @AdultBirthDate
    private LocalDate birthDate;

    @Email(message = "Invalid email format", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @NotBlank(message = "Email must not be null")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;

    @NotBlank(message = "Phone number must not be null")
    @Pattern(regexp = "^0(?:50|51|55|70|77|10|60)\\d{7}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Pattern(regexp = "\\d{5}", message = "CIF must be a 5-digit number")
    private String cif;

    private String role;


//    private List<AccountRequest> accounts;
}
