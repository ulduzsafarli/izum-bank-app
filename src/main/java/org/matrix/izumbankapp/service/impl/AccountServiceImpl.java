package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.matrix.izumbankapp.enumeration.NotificationType;
import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.enumeration.accounts.AccountType;
import org.matrix.izumbankapp.exception.*;
import org.matrix.izumbankapp.exception.accounts.*;
import org.matrix.izumbankapp.mapper.AccountMapper;
import org.matrix.izumbankapp.model.notifications.NotificationRequest;
import org.matrix.izumbankapp.model.accounts.*;
import org.matrix.izumbankapp.dao.repository.AccountRepository;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.users.UserAccountsResponse;
import org.matrix.izumbankapp.service.*;
import org.matrix.izumbankapp.util.GenerateRandom;
import org.matrix.izumbankapp.util.specifications.AccountSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
    public Page<AccountResponse> findAccountsByFilter(AccountFilterDto accountFilterDto, Pageable pageRequest) {
        log.info("Searching accounts by filter: {}", accountFilterDto);
        try {
            Specification<AccountEntity> accountSpecification = AccountSpecifications.getAccountSpecification(accountFilterDto);
            Page<AccountEntity> accountEntityPage = accountRepository.findAll(accountSpecification, pageRequest);
            log.info("Successfully found accounts");
            return accountEntityPage.map(accountMapper::toDto);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to find an account in the database", ex);
        }
    }

    @Override
    public AccountResponse getAccountById(Long accountId) {
        log.info("Retrieving account by ID: {}", accountId);
        try {
            AccountEntity accountEntity = accountRepository.findById(accountId)
                    .orElseThrow(() -> new NotFoundException(String.format(WITH_ID_NOT_FOUND, accountId)));
            AccountResponse accountResponse = accountMapper.toDto(accountEntity);
            log.info("Successfully retrieved account");
            return accountResponse;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to get accounts by ID from the database", ex);
        }
    }

    @Override
    public AccountResponse getAccountByAccountNumber(String accountNumber) {
        log.info("Retrieving account by account number: {}", accountNumber);
        try {
            AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));
            AccountResponse accountResponse = accountMapper.toDto(accountEntity);
            log.info("Successfully retrieved account");
            return accountResponse;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to get accounts by account number from the database", ex);
        }
    }

    @Override
    public void updateBalance(String accountNumber, BigDecimal newBalance)
            throws NotFoundException, DatabaseException, InsufficientFundsException {

        AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            sendNotification(account.getId(), "Insufficient funds in the account for updating your money",
                    NotificationType.ALERT);
            throw new InsufficientFundsException("Insufficient funds in the account");
        }

        account.setCurrentBalance(newBalance);
        try {
            accountRepository.save(account);
            sendNotification(account.getId(), "Your balance updating successfully", NotificationType.UPDATE);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Error saving account update", ex);
        }
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
    public List<AccountResponse> getAllAccounts() {
        log.info("Retrieving all accounts");
        try {
            var accountDtoList = accountRepository.findAll().stream().map(accountMapper::toDto).toList();
            log.info("Successfully retrieved all accounts");
            return accountDtoList;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to get all accounts from the database", ex);
        }
    }

    @Override
    @Transactional
    public AccountResponse createAccount(AccountCreateDto account) {
        log.info("Creating account for user: {}", account.userId());
        try {
            userService.createCif(account.userId());
            AccountEntity accountEntity = accountMapper.fromRequestDtoForUser(account);
            accountEntity.setStatus(AccountStatus.ACTIVE);
            accountEntity.setPin(passwordEncoder.encode(account.pin()));
            accountEntity.setAccountNumber(GenerateRandom.generateAccountNumber());
            accountRepository.save(accountEntity);
            sendNotification(accountEntity.getId(),
                    "Your account has been successfully created. Details:\n" + accountEntity,
                    NotificationType.MESSAGE);
            log.info("Account created successfully");
            return accountMapper.toDto(accountEntity);
        } catch (NotFoundException ex) {
            throw new NotFoundException("Failed to find user with ID: " + account.userId());
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to add new account to the database", ex);
        }
    }


    @Override
    @Transactional
    public ResponseDto updateAccount(Long accountId, AccountRequest account) {
        log.info("Updating account by ID: {}", accountId);

        var updateAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(String.format(WITH_ID_NOT_FOUND, accountId)));
        accountMapper.updateEntityFromDto(account, updateAccount);
        accountRepository.save(updateAccount);

        sendNotification(updateAccount.getId(), "Successfully update your account. Details:\n" + updateAccount,
                NotificationType.MESSAGE);

        log.info("Successfully updated account {}", account);
        return new ResponseDto("Account updated successfully");
    }

    @Override
    @Transactional
    public ResponseDto deleteAccount(Long accountId) {
        log.info("Deleting account by ID: {}", accountId);
        if (accountRepository.existsById(accountId)) {
            accountRepository.deleteById(accountId);
            log.info("Account deleted successfully");
            return new ResponseDto("Account deleted successfully");
        } else {
            throw new NotFoundException(String.format(WITH_ID_NOT_FOUND, accountId));
        }
    }

    @Override
    public ResponseDto closeAccount(String accountNumber) {
        log.info("Closing account: {}", accountNumber);

        var account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));

        if (BigDecimal.valueOf(Double.parseDouble(getBalance(accountNumber))).compareTo(BigDecimal.ZERO) != 0) {
            throw new AccountClosingException("The balance must be zero to close the account");
        }
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
        sendNotification(account.getId(), "Your account is closed", NotificationType.ALERT);
        return new ResponseDto("Account closed successfully");
    }


    @Override
    public String getBalance(String accountNumber) {
        log.info("Getting current balance from account {}", accountNumber);
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));
        log.info("Get current balance from account {} successfully", accountNumber);
        return account.getCurrentBalance().toString();
    }


    @Override
    public ResponseDto updateStatus(String accountNumber, AccountStatus accountUpdate) {
        log.info("Updating status for account {}", accountNumber);
        var account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        if (account.getStatus().equals(accountUpdate)) {
            throw new AccountStatusException("Account is already " + account.getStatus());
        }
        account.setStatus(accountUpdate);
        accountRepository.save(account);
        sendNotification(account.getId(), "The status of your account is updated", NotificationType.ALERT);
        return new ResponseDto("Account updated successfully");
    }

    @Override
    public UserAccountsResponse getUserByAccountNumber(String accountNumber) {
        log.info("Reading user by account number {}", accountNumber);

        try {
            var userAccountsDto = accountRepository.findByAccountNumber(accountNumber)
                    .map(AccountEntity::getUser)
                    .map(userEntity -> userService.getUserByIdForAccount(userEntity.getId()))
                    .orElseThrow(() -> new NotFoundException(String.format(WITH_NUMBER_NOT_FOUND, accountNumber)));
            log.info("Read user by account number {} successfully", accountNumber);
            return userAccountsDto;
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to read user by account number", ex);
        }
    }

    @Override
    public void saveAccount(AccountResponse account) {
        log.info("Saving account {}", account);
        var accountEntity = accountRepository.findById(account.getId())
                .orElseThrow(() -> new NotFoundException(String.format(WITH_ID_NOT_FOUND, account.getId())));
        accountRepository.save(accountEntity);
        log.info("Successfully save account {}", account);
    }

    @Override
    public List<AccountResponse> getDepositAccountsCreatedOnDate(int dayOfMonth) {
        log.info("Receiving deposit accounts created on the {} day", dayOfMonth);
        var accountEntities = accountRepository.findAccountsByDateAndTypeAndStatus(
                        AccountType.DEPOSIT, AccountStatus.ACTIVE, LocalDate.now(), dayOfMonth)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND)));

        return accountEntities.stream().map(accountMapper::toDto).toList();
    }


    private void sendNotification(Long userId, String message, NotificationType notificationType) {
        var notification = NotificationRequest.builder()
                .message(message)
                .type(notificationType)
                .userId(userId).build();
        notificationService.createNotification(notification);
    }

}