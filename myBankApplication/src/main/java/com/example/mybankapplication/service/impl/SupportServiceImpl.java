package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.dao.entities.SupportEntity;
import com.example.mybankapplication.dao.repository.SupportRepository;
import com.example.mybankapplication.exception.EmailSendingException;
import com.example.mybankapplication.exception.NotFoundException;
import com.example.mybankapplication.mapper.SupportMapper;
import com.example.mybankapplication.model.support.SupportDto;
import com.example.mybankapplication.model.support.SupportAnswerDto;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.model.support.SupportResponseDto;
import com.example.mybankapplication.service.SupportService;
import com.example.mybankapplication.util.EmailSending;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;
    private final SupportMapper supportMapper;
    private final EmailSending emailSending;

    @Value("200")
    private String responseCodeSuccess;

    @Override
    public ResponseDto sendSupport(SupportDto supportDto) {
        log.info("Processing support request: {}", supportDto);

        try {
            supportRepository.save(supportMapper.toEntity(supportDto));
            emailSending.sendSupportEmail(supportDto);
            log.info("Contact form sent successfully: {}", supportDto);
            return ResponseDto.builder()
                    .responseMessage("Form submitted successfully")
                    .responseCode(responseCodeSuccess).build();
        } catch (RuntimeException e) {
            throw new EmailSendingException("Error while processing support request", e);
        }
    }

    @Override
    public ResponseDto sendResponse(Long supportID, SupportAnswerDto supportAnswerDto) {
        log.info("Sending answer for support request: {}", supportAnswerDto);
        SupportEntity supportEntity = supportRepository.findById(supportID)
                .orElseThrow(() -> new NotFoundException("Support message not found with ID " + supportID));

        try {
            emailSending.sendResponseEmail(supportEntity.getEmail(), supportAnswerDto);
            supportEntity.setAnswered(true);
            supportRepository.save(supportEntity);
            log.info("Response email sent successfully to {}", supportEntity.getEmail());
            return ResponseDto.builder()
                    .responseMessage("Form responses successfully")
                    .responseCode(responseCodeSuccess).build();
        } catch (RuntimeException e) {
            log.error("Failed to send response email to {}", supportEntity.getEmail(), e);
            throw new EmailSendingException("Error while sending response email", e);
        }
    }

    @Override
    public List<SupportResponseDto> getAllSupportRequests() {
        log.info("Retrieving all support requests");
        var supports = supportRepository.findAll().stream().map(supportMapper::toResponseList).toList();
        log.info("Successfully retrieve all support requests");
        return supports;
    }

    @Override
    public List<SupportResponseDto> getUnAnsweredSupportRequests() {
        log.info("Retrieving all support requests");
        var supports = supportRepository.findUnAnsweredRequests().stream().map(supportMapper::toResponseList).toList();
        log.info("Successfully retrieve all support requests");
        return supports;
    }
}
