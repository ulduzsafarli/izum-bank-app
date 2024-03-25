package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.UserProfileEntity;
import com.example.mybankapplication.model.users.profile.UserProfileDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-24T23:52:11+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class UserProfileMapperImpl implements UserProfileMapper {

    @Override
    public UserProfileDto toDto(UserProfileEntity userProfileEntity) {
        if ( userProfileEntity == null ) {
            return null;
        }

        UserProfileDto.UserProfileDtoBuilder userProfileDto = UserProfileDto.builder();

        userProfileDto.firstName( userProfileEntity.getFirstName() );
        userProfileDto.lastName( userProfileEntity.getLastName() );
        userProfileDto.birthDate( userProfileEntity.getBirthDate() );
        userProfileDto.phoneNumber( userProfileEntity.getPhoneNumber() );
        userProfileDto.gender( userProfileEntity.getGender() );
        userProfileDto.nationality( userProfileEntity.getNationality() );

        return userProfileDto.build();
    }

    @Override
    public UserProfileEntity toEntity(UserProfileDto userProfileDto) {
        if ( userProfileDto == null ) {
            return null;
        }

        UserProfileEntity.UserProfileEntityBuilder userProfileEntity = UserProfileEntity.builder();

        userProfileEntity.firstName( userProfileDto.getFirstName() );
        userProfileEntity.lastName( userProfileDto.getLastName() );
        userProfileEntity.birthDate( userProfileDto.getBirthDate() );
        userProfileEntity.phoneNumber( userProfileDto.getPhoneNumber() );
        userProfileEntity.gender( userProfileDto.getGender() );
        userProfileEntity.nationality( userProfileDto.getNationality() );

        return userProfileEntity.build();
    }
}
