package org.matrix.izumbankapp.controller;

import org.matrix.izumbankapp.model.support.SupportDto;
import org.matrix.izumbankapp.model.support.EmailAnswerDto;
import org.matrix.izumbankapp.model.support.SupportResponseDto;
import org.matrix.izumbankapp.service.SupportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/support")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportService;

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendRequest(@Valid @RequestBody SupportDto supportDto) {
        supportService.sendRequest(supportDto);
    }

    @PostMapping("/respond/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void sendResponse(@PathVariable Long id, @Valid @RequestBody EmailAnswerDto emailAnswerDto) {
        supportService.sendResponse(id, emailAnswerDto);
    }

    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<SupportResponseDto> getRequests(){
        return supportService.getRequests();
    }
    @GetMapping("/unanswered-requests")
    @ResponseStatus(HttpStatus.OK)
    public List<SupportResponseDto> getUnAnsweredRequests(){
        return supportService.getUnAnsweredRequests();
    }


}