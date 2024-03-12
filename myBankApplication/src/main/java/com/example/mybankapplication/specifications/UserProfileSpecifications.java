package com.example.mybankapplication.specifications;

import com.example.mybankapplication.dao.entities.UserProfileEntity;
import com.example.mybankapplication.model.users.profile.UserProfileFilterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserProfileSpecifications {

    private UserProfileSpecifications(){}
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

    public static Specification<UserProfileEntity> getUserProfileSpecification(UserProfileFilterDto userProfileFilterDto) {
        log.debug("Get specification for user {}", userProfileFilterDto.toString());
        return Specification.<UserProfileEntity>where(
                        likeIgnoreCase("firstName", userProfileFilterDto.getFirstName()))
                .and(likeIgnoreCase("lastName", userProfileFilterDto.getLastName()))
                .and(isEqual("birthDate", userProfileFilterDto.getBirthDate()))
                .and(isEqual("phoneNumber", userProfileFilterDto.getPhoneNumber()));
    }
}
