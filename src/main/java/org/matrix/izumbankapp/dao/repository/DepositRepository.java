package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.DepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DepositRepository extends JpaRepository<DepositEntity, Long> {
    DepositEntity findByAccountId(Long id);

//    @EntityGraph("Deposit.withAccount")
//    List<DepositEntity> findDepositAccountsCreatedOnDate(@Param("dayOfMonth") int dayOfMonth);
//    @Query("SELECT d FROM DepositEntity d LEFT JOIN FETCH d.account WHERE FUNCTION('DAY', d.account.createdAt) = :dayOfMonth")
//    List<DepositEntity> findDepositsCreatedOnDate(@Param("dayOfMonth") int dayOfMonth);

    @Query("SELECT d FROM DepositEntity d LEFT JOIN FETCH d.account WHERE EXTRACT(DAY FROM d.account.createdAt) = :dayOfMonth")
    List<DepositEntity> findDepositsCreatedOnDate(@Param("dayOfMonth") int dayOfMonth);

}