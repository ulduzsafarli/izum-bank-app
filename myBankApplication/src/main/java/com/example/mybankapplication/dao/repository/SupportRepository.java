package com.example.mybankapplication.dao.repository;

import com.example.mybankapplication.dao.entities.SupportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportRepository extends JpaRepository<SupportEntity, Long> {
}
