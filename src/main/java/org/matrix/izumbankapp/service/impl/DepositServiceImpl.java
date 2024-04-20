package org.matrix.izumbankapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.dao.repository.DepositRepository;
import org.matrix.izumbankapp.enumeration.accounts.AccountType;
import org.matrix.izumbankapp.exception.NotFoundException;
import org.matrix.izumbankapp.mapper.DepositMapper;
import org.matrix.izumbankapp.model.accounts.AccountCreateDto;
import org.matrix.izumbankapp.model.accounts.AccountResponse;
import org.matrix.izumbankapp.model.deposits.DepositRequest;
import org.matrix.izumbankapp.model.deposits.DepositResponse;
import org.matrix.izumbankapp.service.AccountService;
import org.matrix.izumbankapp.service.DepositService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;

    @Override
    public void save(DepositResponse depositResponse) {
        log.info("Saving deposit");
        var deposit = depositMapper.toEntity(depositResponse);
        var account = depositRepository.findById(deposit.getId())
                .orElseThrow(() -> new NotFoundException("Account not found for deposit ID" + deposit.getId()))
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

    @Override
    @Transactional
    public void create(DepositRequest depositRequest) {
        log.info("Creating deposit account for user: {}", depositRequest.getUserId());

        AccountCreateDto accountDto = new AccountCreateDto(
                depositRequest.getUserId(),
                "333",
                depositRequest.getDepositExpireDate(),
                depositRequest.getCurrencyType(),
                AccountType.DEPOSIT,
                calculateInterest(depositRequest.getAmount(), depositRequest.getInterest(), depositRequest.getDepositExpireDate()),
                BigDecimal.ZERO,
                null,
                passwordEncoder.encode(depositRequest.getPin())
        );

        AccountResponse accountResponse = accountService.create(accountDto);

        DepositResponse depositResponse = DepositResponse.builder()
                .account(accountService.getById(accountResponse.getId()))
                .amount(depositRequest.getAmount())
                .interestRate(depositRequest.getInterest())
                .yearlyInterest(depositRequest.isYearlyInterest()).build();

        depositRepository.save(depositMapper.toEntity(depositResponse));
        log.info("Deposit account created successfully");
    }

    private BigDecimal calculateInterest(BigDecimal amount, BigDecimal interest, LocalDate depositExpireDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(currentDate, depositExpireDate);
        int months = period.getMonths() + period.getYears() * 12;

        BigDecimal interestRate = BigDecimal.ZERO.add(interest.divide(BigDecimal.valueOf(100),
                2, RoundingMode.HALF_UP));
        BigDecimal monthlyAmount = amount.multiply(interestRate);
        BigDecimal calculatedInterest = monthlyAmount.multiply(BigDecimal.valueOf(months));

        return amount.add(calculatedInterest);
    }

}
