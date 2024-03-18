package com.example.mybankapplication.mapper;


import com.example.mybankapplication.dao.entities.SupportEntity;
import com.example.mybankapplication.model.support.SupportDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupportMapper {
    SupportEntity toEntity(SupportDto supportDto);
    SupportDto toDto(SupportEntity supportEntity);
}
