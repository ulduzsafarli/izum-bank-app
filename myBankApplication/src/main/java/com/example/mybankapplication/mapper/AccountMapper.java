package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.AccountEntity;
import com.example.mybankapplication.model.accounts.AccountRequest;
import com.example.mybankapplication.model.accounts.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring", uses = {UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
//    @Mapping(source = "customer.id", target = "customerId")
    AccountResponse toDto(AccountEntity accountEntity);

    AccountEntity fromRequestDto(AccountRequest accountRequest);
    AccountEntity fromResponseDto(AccountResponse accountResponse);

    AccountEntity updateEntityFromDto(AccountRequest account, @MappingTarget AccountEntity accountEntity);
}
