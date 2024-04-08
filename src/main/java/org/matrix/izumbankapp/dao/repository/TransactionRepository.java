package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<List<Transaction>> findByAccountId(Long accountId);
    Page<Transaction> findAll(Specification<Transaction> spec, Pageable pageRequest);
    Optional<Transaction> findByTransactionUUID(String transactionUUID);
}
