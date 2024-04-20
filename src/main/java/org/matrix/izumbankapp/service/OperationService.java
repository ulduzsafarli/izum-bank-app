package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.accounts.TransferMoneyRequest;
import org.matrix.izumbankapp.model.accounts.WithdrawalRequest;

public interface OperationService {
    void transferMoney(TransferMoneyRequest transferMoneyRequest);

    void withdrawal(WithdrawalRequest withdrawalRequest);

    void activateDepositScheduler();

    String getBalance(String accountNumber);
}
