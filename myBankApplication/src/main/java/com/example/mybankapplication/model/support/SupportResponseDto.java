package com.example.mybankapplication.model.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupportResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String message;
    private boolean isAnswered;
}
