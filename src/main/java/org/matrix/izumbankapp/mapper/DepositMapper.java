package org.matrix.izumbankapp.mapper;

import org.mapstruct.*;
import org.matrix.izumbankapp.dao.entities.Deposit;
import org.matrix.izumbankapp.model.deposits.DepositResponse;

@Mapper(componentModel = "spring", uses = {AccountMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepositMapper {
    DepositResponse toResponseDto(Deposit deposit);
    @Mapping(source = "account", target = "account")
    Deposit toEntity(DepositResponse depositResponse);
}
