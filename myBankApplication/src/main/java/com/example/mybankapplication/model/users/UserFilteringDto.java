package com.example.mybankapplication.model.users;

import com.example.mybankapplication.model.users.profile.UserProfileFilterDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilteringDto {
    private UserProfileFilterDto userProfileFilterDto;
}
