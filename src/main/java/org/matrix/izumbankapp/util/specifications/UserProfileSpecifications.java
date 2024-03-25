package org.matrix.izumbankapp.util.specifications;

import org.matrix.izumbankapp.dao.entities.UserProfileEntity;
import org.matrix.izumbankapp.model.users.profile.UserProfileFilterDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class UserProfileSpecifications {

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
        return Specification.<UserProfileEntity>where(
                        likeIgnoreCase("firstName", userProfileFilterDto.getFirstName()))
                .and(likeIgnoreCase("lastName", userProfileFilterDto.getLastName()))
                .and(isEqual("birthDate", userProfileFilterDto.getBirthDate()))
                .and(isEqual("phoneNumber", userProfileFilterDto.getPhoneNumber()));
    }
}
