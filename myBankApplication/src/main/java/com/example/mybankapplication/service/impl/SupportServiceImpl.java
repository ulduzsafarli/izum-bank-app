package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.dao.entities.SupportEntity;
import com.example.mybankapplication.dao.repository.SupportRepository;
import com.example.mybankapplication.exception.NotFoundException;
import com.example.mybankapplication.mapper.SupportMapper;
import com.example.mybankapplication.model.support.SupportDto;
import com.example.mybankapplication.model.support.SupportResponseDto;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.service.EmailService;
import com.example.mybankapplication.service.SupportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;
    private final SupportMapper supportMapper;
    private final EmailService emailService;

    @Value("200")
    private String responseCodeSuccess;

    @Override
    public ResponseDto sendSupport(SupportDto supportDto) {
        log.info("Processing support request: {}", supportDto);

        try {
            supportRepository.save(supportMapper.toEntity(supportDto));
            emailService.sendSupportEmail(supportDto);
            log.info("Contact form sent successfully: {}", supportDto);
            return ResponseDto.builder()
                    .responseMessage("Form submitted successfully")
                    .responseCode(responseCodeSuccess).build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while processing support request", e);
        }
    }

    @Override
    public ResponseDto sendResponse(Long supportID, SupportResponseDto supportResponseDto) {
        SupportEntity supportEntity = supportRepository.findById(supportID)
                .orElseThrow(() -> new NotFoundException("Support message not found with ID " + supportID));

        try {
            emailService.sendResponseEmail(supportEntity.getEmail(), supportResponseDto);
            supportEntity.setAnswered(true);
            log.info("Response email sent successfully to {}", supportEntity.getEmail());
            return ResponseDto.builder()
                    .responseMessage("Form responses successfully")
                    .responseCode(responseCodeSuccess).build();
        } catch (RuntimeException e) {
            log.error("Failed to send response email to {}", supportEntity.getEmail(), e);
            throw new RuntimeException("Error while sending response email", e);
        }
    }
}
