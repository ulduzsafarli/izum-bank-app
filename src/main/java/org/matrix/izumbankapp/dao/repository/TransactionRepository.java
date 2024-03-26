package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.TransactionEntity;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<List<TransactionEntity>> findByAccountId(Long accountId);
    Page<TransactionEntity> findAll(Specification<TransactionEntity> spec, Pageable pageRequest);
    Optional<List<TransactionEntity>> findByType(TransactionType type);
}
