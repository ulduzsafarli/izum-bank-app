package com.example.mybankapplication.dao.entities;

import com.example.mybankapplication.dao.entities.abstractentity.Auditable;
import com.example.mybankapplication.enumeration.accounts.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "accounts", schema = "public")
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 3)
    private String branchCode;

    @Column(unique = true, nullable = false, length = 7)
    private String accountNumber;

    @Column(nullable = false)
    private LocalDate accountExpireDate;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'Active'")
    private AccountStatus status;

    @Column(nullable = false, columnDefinition = "NUMERIC(38,2) USING available_balance::numeric(38,2)")
    private BigDecimal availableBalance;

    @Column(nullable = false, columnDefinition = "NUMERIC(38,2) USING current_balance::numeric(38,2)")
    private BigDecimal currentBalance;

    @Column(length = 4, nullable = false)
    private String pin;

    private BigDecimal transactionLimit;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
