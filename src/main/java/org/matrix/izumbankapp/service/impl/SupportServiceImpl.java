package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.Support;
import org.matrix.izumbankapp.dao.repository.SupportRepository;
import org.matrix.izumbankapp.exception.NotFoundException;
import org.matrix.izumbankapp.mapper.SupportMapper;
import org.matrix.izumbankapp.model.support.SupportDto;
import org.matrix.izumbankapp.model.support.EmailAnswerDto;
import org.matrix.izumbankapp.model.support.SupportResponseDto;
import org.matrix.izumbankapp.service.EmailSendingService;
import org.matrix.izumbankapp.service.SupportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;
    private final SupportMapper supportMapper;
    private final EmailSendingService emailSendingService;

    @Override
    public void sendRequest(SupportDto supportDto) {
        log.info("Processing support request: {}", supportDto);

        supportRepository.save(supportMapper.toEntity(supportDto));
        emailSendingService.sendSupportEmail(supportDto);
        log.info("Contact form sent successfully: {}", supportDto);
    }

    @Override
    public void sendResponse(Long supportID, EmailAnswerDto emailAnswerDto) {
        log.info("Sending answer for support request: {}", emailAnswerDto);
        Support support = supportRepository.findById(supportID)
                .orElseThrow(() -> new NotFoundException("Support message not found with ID " + supportID));

        emailSendingService.sendResponseEmail(support.getEmail(), emailAnswerDto);
        support.setAnswered(true);
        supportRepository.save(support);
        log.info("Response email sent successfully to {}", support.getEmail());
    }

    @Override
    public List<SupportResponseDto> getRequests() {
        log.info("Retrieving all support requests");
        var supports = supportRepository.findAll().stream().map(supportMapper::toResponseList).toList();
        log.info("Successfully retrieve all support requests");
        return supports;
    }

    @Override
    public List<SupportResponseDto> getUnAnsweredRequests() {
        log.info("Retrieving all support requests");
        var supports = supportRepository.findUnAnsweredRequests().stream().map(supportMapper::toResponseList).toList();
        log.info("Successfully retrieve all support requests");
        return supports;
    }
}
