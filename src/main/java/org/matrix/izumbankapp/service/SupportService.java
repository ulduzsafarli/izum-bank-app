package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.support.SupportDto;
import org.matrix.izumbankapp.model.support.SupportAnswerDto;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.support.SupportResponseDto;

import java.util.List;

public interface SupportService {
    ResponseDto sendSupport(SupportDto supportDto);

    ResponseDto sendResponse(Long supportID, SupportAnswerDto supportAnswerDto);

    List<SupportResponseDto> getAllSupportRequests();

    List<SupportResponseDto> getUnAnsweredSupportRequests();
}
