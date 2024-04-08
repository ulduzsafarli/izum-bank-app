package org.matrix.izumbankapp.util.specifications;

import org.matrix.izumbankapp.dao.entities.Account;
import org.matrix.izumbankapp.model.accounts.AccountFilterDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import static org.matrix.izumbankapp.util.specifications.SpecificationUtil.*;

@UtilityClass
public class AccountSpecifications {
    public static Specification<Account> getAccountSpecification(AccountFilterDto filter) {

        var spec = Specification.<Account>where(
                        likeIgnoreCase("branchCode", filter.getBranchCode()))
                .and(isEqual("accountNumber", filter.getAccountNumber()))
                .and(isEqual("currencyType", filter.getCurrencyType()))
                .and(isEqual("accountType", filter.getAccountType()))
                .and(isEqual("status", filter.getStatus()))
                .and(isEqual("currentBalance", filter.getCurrentBalance()))
                .and(isEqual("transactionLimit", filter.getTransactionLimit()));

        if (filter.getCreatedAt() != null) {
            spec = spec.and(filterByDates(filter.getCreatedAt(), filter.getAccountExpireDate(), "createdAt"));
        }
        if (filter.getAccountExpireDate() != null) {
            spec = spec.and(filterByDates(filter.getCreatedAt(), filter.getAccountExpireDate(), "accountExpireDate"));
        }

        return spec;
    }
}
