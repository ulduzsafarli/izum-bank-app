package com.example.mybankapplication.mapper;

import com.example.mybankapplication.dao.entities.AccountEntity;
import com.example.mybankapplication.dao.entities.UserEntity;
import com.example.mybankapplication.dao.entities.UserProfileEntity;
import com.example.mybankapplication.model.accounts.AccountRequest;
import com.example.mybankapplication.model.accounts.AccountResponse;
import com.example.mybankapplication.model.users.UserAccountsResponse;
import com.example.mybankapplication.model.users.UserCreateDto;
import com.example.mybankapplication.model.users.UserRequest;
import com.example.mybankapplication.model.users.UserResponse;
import com.example.mybankapplication.model.users.UserUpdateDto;
import com.example.mybankapplication.model.users.profile.UserProfileDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-25T00:48:57+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setId( userEntity.getId() );
        userResponse.setEmail( userEntity.getEmail() );
        userResponse.setCif( userEntity.getCif() );
        userResponse.setRole( userEntity.getRole() );
        userResponse.setUserProfile( userProfileEntityToUserProfileDto( userEntity.getUserProfile() ) );

        return userResponse;
    }

    @Override
    public UserAccountsResponse toAccountsDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserAccountsResponse userAccountsResponse = new UserAccountsResponse();

        userAccountsResponse.setId( userEntity.getId() );
        userAccountsResponse.setEmail( userEntity.getEmail() );
        userAccountsResponse.setCif( userEntity.getCif() );
        userAccountsResponse.setRole( userEntity.getRole() );
        userAccountsResponse.setAccounts( accountEntityListToAccountResponseList( userEntity.getAccounts() ) );

        return userAccountsResponse;
    }

    @Override
    public UserEntity toEntity(UserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.email( userRequest.getEmail() );
        userEntity.password( userRequest.getPassword() );
        userEntity.cif( userRequest.getCif() );
        userEntity.userProfile( userProfileDtoToUserProfileEntity( userRequest.getUserProfile() ) );

        return userEntity.build();
    }

    @Override
    public UserEntity toEntity(UserCreateDto userCreateDto) {
        if ( userCreateDto == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.email( userCreateDto.getEmail() );
        userEntity.password( userCreateDto.getPassword() );
        userEntity.userProfile( userProfileDtoToUserProfileEntity( userCreateDto.getUserProfile() ) );

        return userEntity.build();
    }

    @Override
    public UserEntity toEntity(UserResponse userResponse) {
        if ( userResponse == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( userResponse.getId() );
        userEntity.email( userResponse.getEmail() );
        userEntity.cif( userResponse.getCif() );
        userEntity.role( userResponse.getRole() );
        userEntity.userProfile( userProfileDtoToUserProfileEntity( userResponse.getUserProfile() ) );

        return userEntity.build();
    }

    @Override
    public List<UserResponse> toDtoList(List<UserEntity> userEntityList) {
        if ( userEntityList == null ) {
            return null;
        }

        List<UserResponse> list = new ArrayList<UserResponse>( userEntityList.size() );
        for ( UserEntity userEntity : userEntityList ) {
            list.add( toDto( userEntity ) );
        }

        return list;
    }

    @Override
    public UserEntity updateEntityFromRequest(UserUpdateDto userCreateDto, UserEntity userEntity) {
        if ( userCreateDto == null ) {
            return userEntity;
        }

        if ( userCreateDto.getEmail() != null ) {
            userEntity.setEmail( userCreateDto.getEmail() );
        }
        if ( userCreateDto.getUserProfile() != null ) {
            if ( userEntity.getUserProfile() == null ) {
                userEntity.setUserProfile( UserProfileEntity.builder().build() );
            }
            userProfileDtoToUserProfileEntity1( userCreateDto.getUserProfile(), userEntity.getUserProfile() );
        }

        return userEntity;
    }

    @Override
    public UserEntity fromUserAccountsDto(UserAccountsResponse userAccountsResponse) {
        if ( userAccountsResponse == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( userAccountsResponse.getId() );
        userEntity.email( userAccountsResponse.getEmail() );
        userEntity.cif( userAccountsResponse.getCif() );
        userEntity.role( userAccountsResponse.getRole() );
        userEntity.accounts( accountResponseListToAccountEntityList( userAccountsResponse.getAccounts() ) );

        return userEntity.build();
    }

    @Override
    public UserEntity toEntityForAccount(UserResponse user, AccountRequest account) {
        if ( user == null && account == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        if ( user != null ) {
            userEntity.id( user.getId() );
            userEntity.email( user.getEmail() );
            userEntity.cif( user.getCif() );
            userEntity.role( user.getRole() );
            userEntity.userProfile( userProfileDtoToUserProfileEntity( user.getUserProfile() ) );
        }

        return userEntity.build();
    }

    @Override
    public UserEntity mapUserForAccount(UserAccountsResponse user) {
        if ( user == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( user.getId() );
        userEntity.email( user.getEmail() );
        userEntity.cif( user.getCif() );
        userEntity.role( user.getRole() );
        userEntity.accounts( accountResponseListToAccountEntityList( user.getAccounts() ) );

        return userEntity.build();
    }

    protected UserProfileDto userProfileEntityToUserProfileDto(UserProfileEntity userProfileEntity) {
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

    protected AccountResponse accountEntityToAccountResponse(AccountEntity accountEntity) {
        if ( accountEntity == null ) {
            return null;
        }

        AccountResponse.AccountResponseBuilder accountResponse = AccountResponse.builder();

        accountResponse.id( accountEntity.getId() );
        accountResponse.branchCode( accountEntity.getBranchCode() );
        accountResponse.accountNumber( accountEntity.getAccountNumber() );
        accountResponse.accountExpireDate( accountEntity.getAccountExpireDate() );
        accountResponse.currencyType( accountEntity.getCurrencyType() );
        accountResponse.accountType( accountEntity.getAccountType() );
        accountResponse.status( accountEntity.getStatus() );
        accountResponse.availableBalance( accountEntity.getAvailableBalance() );
        accountResponse.currentBalance( accountEntity.getCurrentBalance() );
        accountResponse.transactionLimit( accountEntity.getTransactionLimit() );

        return accountResponse.build();
    }

    protected List<AccountResponse> accountEntityListToAccountResponseList(List<AccountEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<AccountResponse> list1 = new ArrayList<AccountResponse>( list.size() );
        for ( AccountEntity accountEntity : list ) {
            list1.add( accountEntityToAccountResponse( accountEntity ) );
        }

        return list1;
    }

    protected UserProfileEntity userProfileDtoToUserProfileEntity(UserProfileDto userProfileDto) {
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

    protected void userProfileDtoToUserProfileEntity1(UserProfileDto userProfileDto, UserProfileEntity mappingTarget) {
        if ( userProfileDto == null ) {
            return;
        }

        if ( userProfileDto.getFirstName() != null ) {
            mappingTarget.setFirstName( userProfileDto.getFirstName() );
        }
        if ( userProfileDto.getLastName() != null ) {
            mappingTarget.setLastName( userProfileDto.getLastName() );
        }
        if ( userProfileDto.getBirthDate() != null ) {
            mappingTarget.setBirthDate( userProfileDto.getBirthDate() );
        }
        if ( userProfileDto.getPhoneNumber() != null ) {
            mappingTarget.setPhoneNumber( userProfileDto.getPhoneNumber() );
        }
        if ( userProfileDto.getGender() != null ) {
            mappingTarget.setGender( userProfileDto.getGender() );
        }
        if ( userProfileDto.getNationality() != null ) {
            mappingTarget.setNationality( userProfileDto.getNationality() );
        }
    }

    protected AccountEntity accountResponseToAccountEntity(AccountResponse accountResponse) {
        if ( accountResponse == null ) {
            return null;
        }

        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setId( accountResponse.getId() );
        accountEntity.setBranchCode( accountResponse.getBranchCode() );
        accountEntity.setAccountNumber( accountResponse.getAccountNumber() );
        accountEntity.setAccountExpireDate( accountResponse.getAccountExpireDate() );
        accountEntity.setCurrencyType( accountResponse.getCurrencyType() );
        accountEntity.setAccountType( accountResponse.getAccountType() );
        accountEntity.setStatus( accountResponse.getStatus() );
        accountEntity.setAvailableBalance( accountResponse.getAvailableBalance() );
        accountEntity.setCurrentBalance( accountResponse.getCurrentBalance() );
        accountEntity.setTransactionLimit( accountResponse.getTransactionLimit() );

        return accountEntity;
    }

    protected List<AccountEntity> accountResponseListToAccountEntityList(List<AccountResponse> list) {
        if ( list == null ) {
            return null;
        }

        List<AccountEntity> list1 = new ArrayList<AccountEntity>( list.size() );
        for ( AccountResponse accountResponse : list ) {
            list1.add( accountResponseToAccountEntity( accountResponse ) );
        }

        return list1;
    }
}
