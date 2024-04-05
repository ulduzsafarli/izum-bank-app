package org.matrix.izumbankapp.mapper;


import org.matrix.izumbankapp.dao.entities.Support;
import org.matrix.izumbankapp.model.support.SupportDto;
import org.matrix.izumbankapp.model.support.SupportResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupportMapper {
    Support toEntity(SupportDto supportDto);
    SupportResponseDto toResponseList(Support support);
}
