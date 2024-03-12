package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.UserEntity;
import com.example.mybankapplication.model.users.UserRequest;
import com.example.mybankapplication.model.users.UserRequestDto;
import com.example.mybankapplication.model.users.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserResponse toDto(UserEntity userEntity);

    UserEntity toEntity(UserRequest userRequest);
    UserEntity toEntity(UserRequestDto userRequestDto);

    UserEntity toEntity(UserResponse userResponse);

    List<UserResponse> toDtoList(List<UserEntity> userEntityList);

    UserEntity updateEntityFromRequest(UserRequestDto userRequestDto, @MappingTarget UserEntity userEntity);
}
