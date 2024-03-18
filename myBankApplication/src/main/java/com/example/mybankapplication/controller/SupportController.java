package com.example.mybankapplication.controller;

import com.example.mybankapplication.model.support.SupportDto;
import com.example.mybankapplication.model.support.SupportResponseDto;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.service.SupportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportService;

    @PostMapping("/support")
    public ResponseEntity<ResponseDto> submitSupportForm(@RequestBody @Valid SupportDto supportDto) {
        return ResponseEntity.status(HttpStatus.OK).body(supportService.sendSupport(supportDto));
    }

    @PostMapping("/support/respond")
    public ResponseEntity<ResponseDto> respondToSupportRequest(@RequestParam Long supportId,
                                                               SupportResponseDto supportResponseDto) {
        return ResponseEntity.status(HttpStatus.OK).body(supportService.sendResponse(supportId, supportResponseDto));
    }


}