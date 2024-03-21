package com.example.mybankapplication.controller;

import com.example.mybankapplication.model.support.SupportDto;
import com.example.mybankapplication.model.support.SupportAnswerDto;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.model.support.SupportResponseDto;
import com.example.mybankapplication.service.SupportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/support")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportService;

    @PostMapping
    public ResponseEntity<ResponseDto> submitSupportForm(@RequestBody @Valid SupportDto supportDto) {
        return ResponseEntity.status(HttpStatus.OK).body(supportService.sendSupport(supportDto));
    }

    @PostMapping("/respond")
    public ResponseEntity<ResponseDto> respondToSupportRequest(@RequestParam Long supportId,
                                                               SupportAnswerDto supportAnswerDto) {
        return ResponseEntity.status(HttpStatus.OK).body(supportService.sendResponse(supportId, supportAnswerDto));
    }

    @GetMapping("/all-requests")
    public ResponseEntity<List<SupportResponseDto>> getAllSupportRequests(){
        return ResponseEntity.status(HttpStatus.OK).body(supportService.getAllSupportRequests());
    }
    @GetMapping("/all-unanswered-requests")
    public ResponseEntity<List<SupportResponseDto>> getUnAnsweredSupportRequests(){
        return ResponseEntity.status(HttpStatus.OK).body(supportService.getUnAnsweredSupportRequests());
    }


}