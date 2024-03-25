package org.matrix.izumbankapp.mapper;


import org.matrix.izumbankapp.dao.entities.SupportEntity;
import org.matrix.izumbankapp.model.support.SupportDto;
import org.matrix.izumbankapp.model.support.SupportResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupportMapper {
    SupportEntity toEntity(SupportDto supportDto);
    SupportResponseDto toResponseList(SupportEntity supportEntity);
}
