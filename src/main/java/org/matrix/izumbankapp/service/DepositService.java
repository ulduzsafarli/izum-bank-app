package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.deposits.DepositResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepositService {
    List<DepositResponse> getAllDeposits();

    void saveDeposits(List<DepositResponse> depositResponses);

    void saveDeposit(DepositResponse depositResponse);

//    List<DepositResponse> getDepositsCreatedOnDate(int dayOfMonth);

    DepositResponse getDepositByAccountId(Long id);
}
