package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.AccountEntity;
import com.example.mybankapplication.dao.entities.UserEntity;
import com.example.mybankapplication.model.accounts.AccountCreateDto;
import com.example.mybankapplication.model.accounts.AccountRequest;
import com.example.mybankapplication.model.accounts.AccountResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-25T00:48:57+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public AccountResponse toDto(AccountEntity accountEntity) {
        if ( accountEntity == null ) {
            return null;
        }

        AccountResponse.AccountResponseBuilder accountResponse = AccountResponse.builder();

        accountResponse.userId( accountEntityUserId( accountEntity ) );
        accountResponse.id( accountEntity.getId() );
        accountResponse.branchCode( accountEntity.getBranchCode() );
        accountResponse.accountNumber( accountEntity.getAccountNumber() );
        accountResponse.accountExpireDate( accountEntity.getAccountExpireDate() );
        accountResponse.currencyType( accountEntity.getCurrencyType() );
        accountResponse.accountType( accountEntity.getAccountType() );
        accountResponse.status( accountEntity.getStatus() );
        accountResponse.availableBalance( accountEntity.getAvailableBalance() );
        accountResponse.currentBalance( accountEntity.getCurrentBalance() );
        accountResponse.transactionLimit( accountEntity.getTransactionLimit() );

        return accountResponse.build();
    }

    @Override
    public AccountEntity fromResponseDto(AccountResponse accountResponse) {
        if ( accountResponse == null ) {
            return null;
        }

        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setId( accountResponse.getId() );
        accountEntity.setBranchCode( accountResponse.getBranchCode() );
        accountEntity.setAccountNumber( accountResponse.getAccountNumber() );
        accountEntity.setAccountExpireDate( accountResponse.getAccountExpireDate() );
        accountEntity.setCurrencyType( accountResponse.getCurrencyType() );
        accountEntity.setAccountType( accountResponse.getAccountType() );
        accountEntity.setStatus( accountResponse.getStatus() );
        accountEntity.setAvailableBalance( accountResponse.getAvailableBalance() );
        accountEntity.setCurrentBalance( accountResponse.getCurrentBalance() );
        accountEntity.setTransactionLimit( accountResponse.getTransactionLimit() );

        return accountEntity;
    }

    @Override
    public AccountEntity fromRequestDtoForUser(AccountCreateDto accountCreateDto) {
        if ( accountCreateDto == null ) {
            return null;
        }

        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setUser( toUserEntity( accountCreateDto.getUserId() ) );
        accountEntity.setBranchCode( accountCreateDto.getBranchCode() );
        accountEntity.setAccountExpireDate( accountCreateDto.getAccountExpireDate() );
        accountEntity.setCurrencyType( accountCreateDto.getCurrencyType() );
        accountEntity.setAccountType( accountCreateDto.getAccountType() );
        accountEntity.setStatus( accountCreateDto.getStatus() );
        accountEntity.setAvailableBalance( accountCreateDto.getAvailableBalance() );
        accountEntity.setCurrentBalance( accountCreateDto.getCurrentBalance() );
        accountEntity.setPin( accountCreateDto.getPin() );
        accountEntity.setTransactionLimit( accountCreateDto.getTransactionLimit() );

        return accountEntity;
    }

    @Override
    public AccountEntity updateEntityFromDto(AccountRequest account, AccountEntity accountEntity) {
        if ( account == null ) {
            return accountEntity;
        }

        if ( account.getBranchCode() != null ) {
            accountEntity.setBranchCode( account.getBranchCode() );
        }
        if ( account.getAccountExpireDate() != null ) {
            accountEntity.setAccountExpireDate( account.getAccountExpireDate() );
        }
        if ( account.getCurrencyType() != null ) {
            accountEntity.setCurrencyType( account.getCurrencyType() );
        }
        if ( account.getAccountType() != null ) {
            accountEntity.setAccountType( account.getAccountType() );
        }
        if ( account.getStatus() != null ) {
            accountEntity.setStatus( account.getStatus() );
        }
        if ( account.getAvailableBalance() != null ) {
            accountEntity.setAvailableBalance( account.getAvailableBalance() );
        }
        if ( account.getCurrentBalance() != null ) {
            accountEntity.setCurrentBalance( account.getCurrentBalance() );
        }
        if ( account.getPin() != null ) {
            accountEntity.setPin( account.getPin() );
        }
        if ( account.getTransactionLimit() != null ) {
            accountEntity.setTransactionLimit( account.getTransactionLimit() );
        }

        return accountEntity;
    }

    private Long accountEntityUserId(AccountEntity accountEntity) {
        if ( accountEntity == null ) {
            return null;
        }
        UserEntity user = accountEntity.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
