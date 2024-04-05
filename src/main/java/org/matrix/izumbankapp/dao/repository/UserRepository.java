package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"userProfile"})
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    @EntityGraph("User.userProfile")
    @Query("SELECT u FROM User u JOIN u.accounts a WHERE a.accountNumber = :accountNumber")
    Optional<User> findByAccountNumber(String accountNumber);
}