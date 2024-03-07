package com.example.mybankapplication.dao.repository;

import com.example.mybankapplication.dao.entities.PassportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassportRepository extends JpaRepository<PassportEntity,Long> {

    Optional<PassportEntity> findByPersonalNo(String finCode);
}
