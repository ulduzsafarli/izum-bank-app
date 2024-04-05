package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Page<UserProfile> findAll(Specification<UserProfile> specifications, Pageable pageRequest);

    boolean existsByPhoneNumber(String phoneNumber);
}
