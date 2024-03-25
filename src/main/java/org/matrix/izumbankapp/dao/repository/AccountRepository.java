package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Page<AccountEntity> findAll(Specification<AccountEntity> spec, Pageable pageRequest);

    boolean existsByAccountNumber(String accountNumber);

    Optional<AccountEntity> findByAccountNumber(String accountNumber);

}