package org.matrix.izumbankapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.dao.repository.DepositRepository;
import org.matrix.izumbankapp.exception.NotFoundException;
import org.matrix.izumbankapp.mapper.DepositMapper;
import org.matrix.izumbankapp.model.deposits.DepositResponse;
import org.matrix.izumbankapp.service.DepositService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {
    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;

    @Override
    public void save(DepositResponse depositResponse) {
        log.info("Saving deposit");
        var deposit = depositMapper.toEntity(depositResponse);
        var account = depositRepository.findById(deposit.getId())
                .orElseThrow(()-> new NotFoundException("Account not found for deposit ID" + deposit.getId()))
                .getAccount();
        account.setCurrentBalance(deposit.getAccount().getCurrentBalance());
        deposit.setAccount(account);
        depositRepository.save(deposit);
        log.info("Deposit saved successfully");
    }

    @Override
    public List<DepositResponse> getByCreatedOnDate(int dayOfMonth) {
        log.info("Fetching deposits created on day of month: {}", dayOfMonth);
        var deposits = depositRepository.findDepositsCreatedOnDate(dayOfMonth)
                .stream().map(depositMapper::toResponseDto).toList();
        log.info("Successfully fetch deposits");
        return deposits;
    }



}
