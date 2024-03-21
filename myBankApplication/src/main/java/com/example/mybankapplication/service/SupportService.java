package com.example.mybankapplication.service;

import com.example.mybankapplication.model.support.SupportDto;
import com.example.mybankapplication.model.support.SupportAnswerDto;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.model.support.SupportResponseDto;

import java.util.List;

public interface SupportService {
    ResponseDto sendSupport(SupportDto supportDto);

    ResponseDto sendResponse(Long supportID, SupportAnswerDto supportAnswerDto);

    List<SupportResponseDto> getAllSupportRequests();

    List<SupportResponseDto> getUnAnsweredSupportRequests();
}
