package org.matrix.izumbankapp.controller;

import org.matrix.izumbankapp.model.support.SupportDto;
import org.matrix.izumbankapp.model.support.EmailAnswerDto;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.support.SupportResponseDto;
import org.matrix.izumbankapp.service.SupportService;
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
                                                               EmailAnswerDto emailAnswerDto) {
        return ResponseEntity.status(HttpStatus.OK).body(supportService.sendResponse(supportId, emailAnswerDto));
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