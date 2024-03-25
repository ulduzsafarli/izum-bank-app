package org.matrix.izumbankapp.model.auth;

import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import lombok.Data;

@Data
public class AccountStatusUpdate {
    AccountStatus accountStatus;
}