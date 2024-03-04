package com.example.mybankapplication.mapper;

import com.example.mybankapplication.entities.CustomerEntity;
import com.example.mybankapplication.model.customers.CustomerRequest;
import com.example.mybankapplication.model.customers.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    CustomerResponse toDto(CustomerEntity customerEntity);

    CustomerEntity toEntity(CustomerRequest customerRequest);

    CustomerEntity toEntity(CustomerResponse customerResponse);

    List<CustomerResponse> toDtoList(List<CustomerEntity> customerEntityList);

    CustomerEntity updateEntityFromRequest(CustomerRequest customer, @MappingTarget CustomerEntity customerEntity);
}
