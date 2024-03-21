package com.example.mybankapplication.dao.repository;

import com.example.mybankapplication.dao.entities.SupportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupportRepository extends JpaRepository<SupportEntity, Long> {

    @Query(value = "SELECT s FROM SupportEntity s WHERE s.isAnswered = false")
    List<SupportEntity> findUnAnsweredRequests();
}
