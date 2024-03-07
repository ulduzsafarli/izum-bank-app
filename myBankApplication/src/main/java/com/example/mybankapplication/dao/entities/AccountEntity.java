package com.example.mybankapplication.dao.entities;

import com.example.mybankapplication.enumeration.AccountStatus;
import com.example.mybankapplication.enumeration.AccountType;
import com.example.mybankapplication.enumeration.CurrencyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts", schema = "public")
@Data
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String branchCode;

    @NotBlank(message = "Account number must not be null")
    @Pattern(regexp = "\\d{7}", message = "Account number must contain 7 digits")
    @Column(unique = true, nullable = false)
    private String accountNumber;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime accountOpenDate;

    @Column(nullable = false)
    private LocalDate accountExpireDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Currency type must not be null")
    private CurrencyType currencyType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(255) DEFAULT 'Active'")
    private AccountStatus status;

    @Column(name = "available_balance", nullable = false, columnDefinition = "NUMERIC(38,2) USING available_balance::numeric(38,2)")
    private BigDecimal availableBalance;

    @Column(name = "current_balance", nullable = false, columnDefinition = "NUMERIC(38,2) USING current_balance::numeric(38,2)")
    private BigDecimal currentBalance;

    private String pin;


    @CreatedBy
    private String createdBy;

    @CreationTimestamp
    private String updatedAt;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
