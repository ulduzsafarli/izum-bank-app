package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.SupportEntity;
import com.example.mybankapplication.model.support.SupportDto;
import com.example.mybankapplication.model.support.SupportResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-24T23:52:11+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class SupportMapperImpl implements SupportMapper {

    @Override
    public SupportEntity toEntity(SupportDto supportDto) {
        if ( supportDto == null ) {
            return null;
        }

        SupportEntity supportEntity = new SupportEntity();

        supportEntity.setFirstName( supportDto.getFirstName() );
        supportEntity.setLastName( supportDto.getLastName() );
        supportEntity.setPhoneNumber( supportDto.getPhoneNumber() );
        supportEntity.setEmail( supportDto.getEmail() );
        supportEntity.setMessage( supportDto.getMessage() );

        return supportEntity;
    }

    @Override
    public SupportResponseDto toResponseList(SupportEntity supportEntity) {
        if ( supportEntity == null ) {
            return null;
        }

        SupportResponseDto supportResponseDto = new SupportResponseDto();

        supportResponseDto.setId( supportEntity.getId() );
        supportResponseDto.setFirstName( supportEntity.getFirstName() );
        supportResponseDto.setLastName( supportEntity.getLastName() );
        supportResponseDto.setPhoneNumber( supportEntity.getPhoneNumber() );
        supportResponseDto.setEmail( supportEntity.getEmail() );
        supportResponseDto.setMessage( supportEntity.getMessage() );
        supportResponseDto.setAnswered( supportEntity.isAnswered() );

        return supportResponseDto;
    }
}
