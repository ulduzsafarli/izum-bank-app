package org.matrix.izumbankapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.matrix.izumbankapp.model.deposits.DepositRequest;
import org.matrix.izumbankapp.service.DepositService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deposits")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void deposit(@Valid @RequestBody DepositRequest depositRequest) {
        depositService.create(depositRequest);
    }
}
