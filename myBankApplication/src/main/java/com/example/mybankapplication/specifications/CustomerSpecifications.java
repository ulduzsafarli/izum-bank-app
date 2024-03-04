package com.example.mybankapplication.specifications;

import com.example.mybankapplication.entities.CustomerEntity;
import com.example.mybankapplication.model.customers.CustomerFilterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerSpecifications {

    private CustomerSpecifications(){}
    private static <T> Specification<T> likeIgnoreCase(String attribute, String value) {
        return (root, query, criteriaBuilder) ->
                value == null || value.isBlank() ?
                        null : criteriaBuilder.like(criteriaBuilder.lower(root.get(attribute)),
                        "%" + value.toLowerCase() + "%");
    }

    private static <T> Specification<T> isEqual(String attribute, Object value) {
        return (root, query, criteriaBuilder) ->
                value == null ?
                        null : criteriaBuilder.equal(root.get(attribute), value);
    }

    public static Specification<CustomerEntity> getCustomerSpecification(CustomerFilterDto customerFilterDto) {
        log.debug("Get specification for customer {}", customerFilterDto.toString());
        return Specification.<CustomerEntity>where(
                        likeIgnoreCase("firstName", customerFilterDto.getFirstName()))
                .and(likeIgnoreCase("lastName", customerFilterDto.getLastName()))
                .and(isEqual("birthDate", customerFilterDto.getBirthDate()));
    }
}
