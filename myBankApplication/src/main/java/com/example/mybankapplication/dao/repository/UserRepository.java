package com.example.mybankapplication.dao.repository;

import com.example.mybankapplication.dao.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
//    Page<UserEntity> findAll(Specification<UserEntity> specifications, Pageable pageRequest);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCif(String randomNumber);

//    void deleteByEmail(String email);
}