package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.userProfile up")
    List<UserEntity> findAllWithProfile();
    @EntityGraph(attributePaths = {"userProfile"})
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
    @EntityGraph("UserEntity.userProfile")
    @Query("SELECT u FROM UserEntity u JOIN u.accounts a WHERE a.accountNumber = :accountNumber")
    Optional<UserEntity> findByAccountNumber(String accountNumber);
}