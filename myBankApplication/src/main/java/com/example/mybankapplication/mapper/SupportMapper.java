package com.example.mybankapplication.mapper;


import com.example.mybankapplication.dao.entities.SupportEntity;
import com.example.mybankapplication.model.support.SupportDto;
import com.example.mybankapplication.model.support.SupportResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupportMapper {
    SupportEntity toEntity(SupportDto supportDto);
    SupportResponseDto toResponseList(SupportEntity supportEntity);
}
