package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.DepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DepositRepository extends JpaRepository<DepositEntity, Long> {
    DepositEntity findByAccountId(Long id);
}
