package org.matrix.izumbankapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.matrix.izumbankapp.model.accounts.TransferMoneyRequest;
import org.matrix.izumbankapp.model.accounts.WithdrawalRequest;
import org.matrix.izumbankapp.model.deposits.DepositRequest;
import org.matrix.izumbankapp.service.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/operations")
public class OperationController {

    private final OperationService operationService;

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void transferToAccount(@Valid @RequestBody TransferMoneyRequest transferMoneyRequest) {
        operationService.transferMoney(transferMoneyRequest);
    }

    @PostMapping("/withdrawal")
    @ResponseStatus(HttpStatus.OK)
    public void withdrawal(@Valid @RequestBody WithdrawalRequest withdrawalRequest) {
       operationService.withdrawal(withdrawalRequest);
    }

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public void deposit(@Valid @RequestBody DepositRequest depositRequest) {
       operationService.createDeposit(depositRequest);
    }

    @PostMapping("/deposit-scheduler")
    @ResponseStatus(HttpStatus.OK)
    public void depositScheduler() {
      operationService.activateDepositScheduler();
    }

    @GetMapping("/{accountNumber}/balance")
    @ResponseStatus(HttpStatus.OK)
    public String getBalance(@PathVariable String accountNumber) {
        return operationService.getBalance(accountNumber);
    }
}
