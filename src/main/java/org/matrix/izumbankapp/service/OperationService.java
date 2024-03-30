package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.accounts.TransferMoneyRequest;
import org.matrix.izumbankapp.model.accounts.WithdrawalRequest;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.deposits.DepositRequest;
import org.springframework.stereotype.Service;

@Service
public interface OperationService {
    ResponseDto transferToAccount(TransferMoneyRequest transferMoneyRequest);

    ResponseDto withdrawal(WithdrawalRequest withdrawalRequest);
    ResponseDto createDepositAccount(DepositRequest depositRequest);
}
