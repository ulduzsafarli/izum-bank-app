package org.matrix.izumbankapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.matrix.izumbankapp.model.accounts.TransferMoneyRequest;
import org.matrix.izumbankapp.model.accounts.WithdrawalRequest;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.deposits.DepositRequest;
import org.matrix.izumbankapp.service.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/operations")
public class OperationController {

    private final OperationService operationService;

    @PostMapping("/transfer")
    public ResponseEntity<ResponseDto> transferToAccount(@Valid @RequestBody TransferMoneyRequest transferMoneyRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(operationService.transferToAccount(transferMoneyRequest));
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<ResponseDto> withdrawal(@Valid @RequestBody WithdrawalRequest withdrawalRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(operationService.withdrawal(withdrawalRequest));
    }

    @PostMapping("/deposit")
    public ResponseEntity<ResponseDto> deposit(@Valid @RequestBody DepositRequest depositRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(operationService.createDepositAccount(depositRequest));
    }

    @PostMapping("/deposit/scheduler")
    public ResponseEntity<ResponseDto> depositScheduler() {
        return ResponseEntity.status(HttpStatus.OK).body(operationService.activateDepositScheduler());
    }

}
