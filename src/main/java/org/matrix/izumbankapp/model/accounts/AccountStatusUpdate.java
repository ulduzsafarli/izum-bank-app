package org.matrix.izumbankapp.model.accounts;

import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import lombok.Data;

@Data
public class AccountStatusUpdate {
    AccountStatus accountStatus;
}