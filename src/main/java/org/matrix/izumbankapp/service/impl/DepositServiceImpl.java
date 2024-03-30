package org.matrix.izumbankapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.dao.entities.DepositEntity;
import org.matrix.izumbankapp.dao.repository.DepositRepository;
import org.matrix.izumbankapp.mapper.DepositMapper;
import org.matrix.izumbankapp.model.deposits.DepositResponse;
import org.matrix.izumbankapp.service.DepositService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {
    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;
    public List<DepositResponse> getAllDeposits(){
        log.info("Receiving all deposits");
        return depositRepository.findAll().stream().map(depositMapper::toResponseDto).toList();
    }

    @Override
    public void saveDeposits(List<DepositResponse> depositResponses) {
        log.info("Saving deposits");
        List<DepositEntity> depositEntities = depositResponses.stream()
                .map(depositMapper::toEntity).toList();
        depositRepository.saveAll(depositEntities);
        log.info("Deposits saved successfully");
    }

    @Override
    public void saveDeposit(DepositResponse depositResponse) {
        log.info("Saving deposit");
        depositRepository.save(depositMapper.toEntity(depositResponse));
        log.info("Deposit saved successfully");
    }

    @Override
    public DepositResponse getDepositByAccountId(Long id) {
        log.info("Receiving deposit by account ID {}", id);
        var depositEntity = depositRepository.findByAccountId(id);
        return depositMapper.toResponseDto(depositEntity);
    }


    public void calculateInterestForAllDeposits() {
        // Получить все депозиты из репозитория
//        var depositAccounts = accountRepository.findAllDeposits().orElseThrow(() -> new NotFoundException(NOT_FOUND));

        var depositResponses = getAllDeposits();

        for (DepositResponse deposit : depositResponses) {
            BigDecimal interestRate = deposit.getInterestRate();

            if (deposit.isYearlyInterest()) {

            } else {
                BigDecimal depositAmount = deposit.getAmount();
                BigDecimal monthlyInterest = depositAmount.multiply(interestRate.divide(BigDecimal.valueOf(100)));

                deposit.setAmount(depositAmount.add(monthlyInterest));
            }
        }
        saveDeposits(depositResponses);

    }
}
