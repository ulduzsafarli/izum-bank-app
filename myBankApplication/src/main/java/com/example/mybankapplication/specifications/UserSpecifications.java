package com.example.mybankapplication.specifications;

import com.example.mybankapplication.dao.entities.UserEntity;
import com.example.mybankapplication.model.users.UserFilterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserSpecifications {

    private UserSpecifications(){}
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

    public static Specification<UserEntity> getUserSpecification(UserFilterDto userFilterDto) {
        log.debug("Get specification for user {}", userFilterDto.toString());
        return Specification.<UserEntity>where(
                        likeIgnoreCase("firstName", userFilterDto.getFirstName()))
                .and(likeIgnoreCase("lastName", userFilterDto.getLastName()))
                .and(isEqual("birthDate", userFilterDto.getBirthDate()));
    }
}
