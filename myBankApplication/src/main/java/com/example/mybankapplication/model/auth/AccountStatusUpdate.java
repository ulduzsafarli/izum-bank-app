package com.example.mybankapplication.model.auth;

import com.example.mybankapplication.enumeration.accounts.AccountStatus;
import lombok.Data;

@Data
public class AccountStatusUpdate {
    AccountStatus accountStatus;
}