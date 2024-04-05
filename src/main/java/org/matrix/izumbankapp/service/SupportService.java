package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.support.SupportDto;
import org.matrix.izumbankapp.model.support.EmailAnswerDto;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.support.SupportResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface SupportService {
    ResponseDto sendSupport(SupportDto supportDto);

    ResponseDto sendResponse(Long supportID, EmailAnswerDto emailAnswerDto);

    List<SupportResponseDto> getAllSupportRequests();

    List<SupportResponseDto> getUnAnsweredSupportRequests();
}
