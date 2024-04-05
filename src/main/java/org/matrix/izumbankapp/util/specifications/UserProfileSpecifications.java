package org.matrix.izumbankapp.util.specifications;

import org.matrix.izumbankapp.dao.entities.UserProfile;
import org.matrix.izumbankapp.model.users.profile.UserProfileFilterDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import static org.matrix.izumbankapp.util.specifications.SpecificationUtil.isEqual;
import static org.matrix.izumbankapp.util.specifications.SpecificationUtil.likeIgnoreCase;

@UtilityClass
public class UserProfileSpecifications {

    public static Specification<UserProfile> getUserProfileSpecification(UserProfileFilterDto userProfileFilterDto) {
        return Specification.<UserProfile>where(
                        likeIgnoreCase("firstName", userProfileFilterDto.getFirstName()))
                .and(likeIgnoreCase("lastName", userProfileFilterDto.getLastName()))
                .and(isEqual("birthDate", userProfileFilterDto.getBirthDate()))
                .and(isEqual("phoneNumber", userProfileFilterDto.getPhoneNumber()));
    }
}
