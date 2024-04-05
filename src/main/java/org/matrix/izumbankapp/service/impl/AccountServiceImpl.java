package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.Account;
import org.matrix.izumbankapp.enumeration.NotificationType;
import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.exception.*;
import org.matrix.izumbankapp.exception.accounts.*;
import org.matrix.izumbankapp.mapper.AccountMapper;
import org.matrix.izumbankapp.model.notifications.NotificationRequest;
import org.matrix.izumbankapp.model.accounts.*;
import org.matrix.izumbankapp.dao.repository.AccountRepository;
import org.matrix.izumbankapp.service.*;
import org.matrix.izumbankapp.util.GenerateRandom;
import org.matrix.izumbankapp.util.specifications.AccountSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private static final String NOT_FOUND = "Account not found.";
    private static final String WITH_ID_NOT_FOUND = "Account with ID %s not found.";
    private static final String WITH_NUMBER_NOT_FOUND = "Account with number %s not found.";


    private final PasswordEncoder passwordEncoder;
    public final AccountRepository accountRepository;
    public final AccountMapper accountMapper;
    private final UserService userService;
    private final NotificationService notificationService;


    @Override
    public Page<AccountResponse> findByFilter(AccountFilterDto accountFilterDto, Pageable pageRequest) {
        log.info("Searching accounts by filter: {}", accountFilterDto);
            Specification<Account> accountSpecification = AccountSpecifications.getAccountSpecification(accountFilterDto);
            Page<Account> accountEntityPage = accountRepository.findAll(accountSpecification, pageRequest);
            log.info("Successfully found accounts");
            return accountEntityPage.map(accountMapper::toDto);
    }

    @Override
    public AccountResponse getById(Long accountId) {
        log.info("Retrieving account by ID: {}", accountId);
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new NotFoundException(String.format(WITH_ID_NOT_FOUND, accountId)));
            AccountResponse accountResponse = accountMapper.toDto(account);
            log.info("Successfully retrieved account");
            return accountResponse;
    }

    @Override
    public AccountResponse getByAccountNumber(String accountNumber) {
        log.info("Retrieving account by account number: {}", accountNumber);
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));
            AccountResponse accountResponse = accountMapper.toDto(account);
            log.info("Successfully retrieved account");
            return accountResponse;
    }

    @Override
    public void updateBalance(String accountNumber, BigDecimal newBalance)
            throws NotFoundException, DatabaseException, InsufficientFundsException {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            sendNotification(account.getId(), "Insufficient funds in the account for updating your money",
                    NotificationType.ALERT);
            throw new InsufficientFundsException("Insufficient funds in the account");
        }
        account.setCurrentBalance(newBalance);
            accountRepository.save(account);
            sendNotification(account.getId(), "Your balance updating successfully", NotificationType.UPDATE);
    }

    @Override
    public void validatePin(AccountResponse account, String pin) throws InvalidPinException {
        log.info("Validating PIN for account number: {}", account.getAccountNumber());
        var accountEntity = accountRepository.findById(account.getId())
                .orElseThrow(() -> new NotFoundException(String.format(WITH_ID_NOT_FOUND, account.getId())));
        boolean isPinValid = passwordEncoder.matches(pin, accountEntity.getPin());
        log.info(isPinValid ? "PIN verification successful." : "PIN verification failed.");
        if (!isPinValid) {
            throw new InvalidPinException("Invalid PIN provided");
        }
    }

    @Override
    @Transactional
    public AccountResponse create(AccountCreateDto account) {
        log.info("Creating account for user: {}", account.userId());
            userService.createCif(account.userId());
            Account accountEntity = accountMapper.fromRequestDtoForUser(account);
            accountEntity.setStatus(AccountStatus.ACTIVE);
            accountEntity.setPin(passwordEncoder.encode(account.pin()));
            accountEntity.setAccountNumber(GenerateRandom.generateAccountNumber());
            accountRepository.save(accountEntity);
            sendNotification(account.userId(),
                    "Your account has been successfully created. Details:\n" + accountEntity,
                    NotificationType.MESSAGE);
            log.info("Account created successfully");
            return accountMapper.toDto(accountEntity);
    }

    @Override
    @Transactional
    public AccountResponse update(Long accountId, AccountRequest account) {
        log.info("Updating account by ID: {}", accountId);

        var updateAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(String.format(WITH_ID_NOT_FOUND, accountId)));
        var accountEntity = accountMapper.updateEntityFromDto(account, updateAccount);
        accountRepository.save(updateAccount);

        sendNotification(updateAccount.getId(), "Successfully update your account. Details:\n" + updateAccount,
                NotificationType.MESSAGE);

        log.info("Successfully updated account {}", account);
        return accountMapper.toDto(accountEntity);
    }

    @Override
    @Transactional
    public void delete(Long accountId) {
        log.info("Deleting account by ID: {}", accountId);
        accountRepository.deleteById(accountId);
        log.info("Account deleted successfully");
    }

    @Override
    public void updateStatus(String accountNumber, AccountStatus accountUpdate) {
        log.info("Updating status for account {}", accountNumber);
        var account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        if (account.getStatus().equals(accountUpdate)) {
            throw new AccountStatusException("Account is already " + account.getStatus());
        }
        account.setStatus(accountUpdate);
        accountRepository.save(account);
        sendNotification(account.getId(), "The status of your account is updated", NotificationType.ALERT);
    }

    private void sendNotification(Long userId, String message, NotificationType notificationType) {
        var notification = NotificationRequest.builder()
                .message(message)
                .type(notificationType)
                .userId(userId).build();
        notificationService.create(notification);
    }

}