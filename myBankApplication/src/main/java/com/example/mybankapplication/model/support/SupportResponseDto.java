package com.example.mybankapplication.model.support;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportResponseDto {
    @NotBlank(message = "Response message must not be null")
    private String responseMessage;
}