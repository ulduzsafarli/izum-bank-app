package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.matrix.izumbankapp.enumeration.accounts.AccountStatus;
import org.matrix.izumbankapp.enumeration.accounts.AccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Page<AccountEntity> findAll(Specification<AccountEntity> spec, Pageable pageRequest);

    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT ae FROM AccountEntity ae " +
            "WHERE ae.accountType = :accountType " +
            "AND ae.status = :status " +
            "AND ae.accountExpireDate > :expireDate " +
            "AND FUNCTION('DAY', ae.createdAt) = :dayOfMonth")
    List<AccountEntity> findAccountsByDateAndTypeAndStatus(
            @Param("accountType") AccountType accountType,
            @Param("status") AccountStatus status,
            @Param("expireDate") LocalDate expireDate,
            @Param("dayOfMonth") int dayOfMonth);

    Optional<AccountEntity> findByAccountNumber(String accountNumber);
    @Query(value = "SELECT a FROM AccountEntity a WHERE a.accountType = 'DEPOSIT'")
    Optional<List<AccountEntity>> findAllDeposits();
}
