package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Page<Account> findAll(Specification<Account> spec, Pageable pageRequest);

    Optional<Account> findByAccountNumber(String accountNumber);
}
