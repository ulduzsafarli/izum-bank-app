package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.deposits.DepositRequest;
import org.matrix.izumbankapp.model.deposits.DepositResponse;

import java.util.List;

public interface DepositService {

    void save(DepositResponse depositResponse);
    List<DepositResponse> getByCreatedOnDate(int dayOfMonth);
    void create(DepositRequest depositRequest);

}
