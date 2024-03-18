package com.example.mybankapplication.service;


import com.example.mybankapplication.model.support.SupportDto;
import com.example.mybankapplication.model.support.SupportResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendResponseEmail(String to, SupportResponseDto supportResponseDto);

    void sendSupportEmail(SupportDto supportDto);
}
