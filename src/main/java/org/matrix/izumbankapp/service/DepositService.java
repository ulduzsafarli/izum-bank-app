package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.deposits.DepositResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepositService {

    void saveDeposit(DepositResponse depositResponse);
    List<DepositResponse> getDepositAccountsCreatedOnDate(int dayOfMonth);

}
