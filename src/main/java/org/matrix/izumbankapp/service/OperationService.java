package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.accounts.TransferMoneyRequest;
import org.matrix.izumbankapp.model.accounts.WithdrawalRequest;
import org.matrix.izumbankapp.model.deposits.DepositRequest;
import org.springframework.stereotype.Service;

@Service
public interface OperationService {
    void transferMoney(TransferMoneyRequest transferMoneyRequest);

    void withdrawal(WithdrawalRequest withdrawalRequest);

    void createDeposit(DepositRequest depositRequest);

    void activateDepositScheduler();

    String getBalance(String accountNumber);
}
