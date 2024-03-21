package com.example.mybankapplication.util.specifications;

import com.example.mybankapplication.dao.entities.AccountEntity;
import com.example.mybankapplication.model.accounts.AccountFilterDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@UtilityClass
public class AccountSpecifications {


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

    public static Specification<AccountEntity> getAccountSpecification(AccountFilterDto accountFilterDto) {
        var spec = Specification.<AccountEntity>where(
                        likeIgnoreCase("branchCode", accountFilterDto.getBranchCode()))
                .and(isEqual("accountExpireDate", accountFilterDto.getAccountExpireDate()))
                .and(isEqual("currency", accountFilterDto.getCurrencyType()))
                .and(isEqual("accountType", accountFilterDto.getAccountType()))
                .and(isEqual("status", accountFilterDto.getStatus()))
                .and(isEqual("currentBalance", accountFilterDto.getCurrentBalance()));

        if (accountFilterDto.getCreatedAt() != null) {
            LocalDateTime startOfDay = accountFilterDto.getCreatedAt().atStartOfDay();
            LocalDateTime endOfDay = accountFilterDto.getCreatedAt().atTime(23, 59, 59);
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("accountOpenDate"), startOfDay, endOfDay));
        }

        return spec;
    }
}

