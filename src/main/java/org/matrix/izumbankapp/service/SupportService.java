package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.support.SupportDto;
import org.matrix.izumbankapp.model.support.EmailAnswerDto;
import org.matrix.izumbankapp.model.support.SupportResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface SupportService {
    void sendRequest(SupportDto supportDto);

    void sendResponse(Long supportID, EmailAnswerDto emailAnswerDto);

    List<SupportResponseDto> getRequests();

    List<SupportResponseDto> getUnAnsweredRequests();
}
