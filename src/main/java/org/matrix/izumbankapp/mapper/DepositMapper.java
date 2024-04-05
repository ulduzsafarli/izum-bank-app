package org.matrix.izumbankapp.mapper;

import org.mapstruct.*;
import org.matrix.izumbankapp.dao.entities.DepositEntity;
import org.matrix.izumbankapp.model.deposits.DepositResponse;

@Mapper(componentModel = "spring", uses = {AccountMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepositMapper {
    DepositResponse toResponseDto(DepositEntity depositEntity);
    @Mapping(source = "account", target = "account")
    DepositEntity toEntity(DepositResponse depositResponse);

}
