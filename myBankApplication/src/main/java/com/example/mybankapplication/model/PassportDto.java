package com.example.mybankapplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PassportDto {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String personalNo;
    private LocalDate expiredDate;
}
