package com.example.mybankapplication.dao.repository;

import com.example.mybankapplication.dao.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
