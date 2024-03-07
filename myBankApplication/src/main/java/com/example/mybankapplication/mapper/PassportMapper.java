package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.PassportEntity;
import com.example.mybankapplication.model.PassportDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassportMapper {
    PassportDto mapToDto(PassportEntity passportEntity);

    PassportEntity mapToEntity(PassportDto passportDto);
}
