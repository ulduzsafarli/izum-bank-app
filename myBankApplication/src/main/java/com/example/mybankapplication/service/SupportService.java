package com.example.mybankapplication.service;

import com.example.mybankapplication.model.support.SupportDto;
import com.example.mybankapplication.model.support.SupportResponseDto;
import com.example.mybankapplication.model.auth.ResponseDto;

public interface SupportService {
    ResponseDto sendSupport(SupportDto supportDto);

    ResponseDto sendResponse(Long supportID, SupportResponseDto supportResponseDto);
}
