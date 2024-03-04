package com.example.mybankapplication.mapper;

import com.example.mybankapplication.entities.PassportEntity;
import com.example.mybankapplication.model.PassportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PassportMapper {
    PassportDto mapToDto(PassportEntity passportEntity);

    PassportEntity mapToEntity(PassportDto passportDto);
}
