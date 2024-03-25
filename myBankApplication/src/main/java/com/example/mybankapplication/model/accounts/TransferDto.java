package com.example.mybankapplication.model.accounts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {
    @Pattern(regexp = "\\d{7}", message = "Account number must contain 7 digits")
    private String fromAccountNumber;
    @NotBlank(message = "Account number must not be null")
    @Pattern(regexp = "\\d{7}", message = "Account number must contain 7 digits")
    private String toAccountNumber;
    @NotBlank(message = "PIN must not be null")
    @Pattern(regexp = "\\d{4}", message = "PIN should contain 4 digits")
    private String pin;
}
