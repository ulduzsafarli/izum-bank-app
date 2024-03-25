package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.UserProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    Page<UserProfileEntity> findAll(Specification<UserProfileEntity> specifications, Pageable pageRequest);

    Optional<UserProfileEntity> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
}
