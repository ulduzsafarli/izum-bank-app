package com.example.mybankapplication.repository;

import com.example.mybankapplication.entities.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Page<CustomerEntity> findAll(Specification<CustomerEntity> specifications,  Pageable pageRequest);

    Optional<CustomerEntity> findByEmail(String email);

    Optional<CustomerEntity> findByPhoneNumber(String phoneNumber);
}