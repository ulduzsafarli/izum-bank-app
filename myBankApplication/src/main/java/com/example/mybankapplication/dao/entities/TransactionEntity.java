package com.example.mybankapplication.dao.entities;

import com.example.mybankapplication.enumeration.transaction.TransactionStatus;
import com.example.mybankapplication.enumeration.transaction.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions", schema = "public")
@Data
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Column(name = "operator_id")
    private Long operatorId;
    @Column(name = "transaction_uuid", unique = true)
    private String transactionUUID;

    //     Limit транзакций: ограничения на ежедневные, еженедельные или ежемесячные транзакции
    @Column(name = "transaction_limit")
    private BigDecimal transactionLimit;

//    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;


    // Процентная ставка: для сберегательных счетов
    // @Column(name = "interest_rate")
    // private BigDecimal interestRate;

    // Overdraft Protection: указывает, есть ли у счета защита от превышения лимита
    // @Column(name = "overdraft_protection")
    // private boolean overdraftProtection;

    // Limit поля: для определенных счетов, лимит на сумму, которую можно снять
    // @Column(name = "withdrawal_limit")
    // private BigDecimal withdrawalLimit;



    // Информация о карте: если счет связан с дебетовой или кредитной картой
    // @Column(name = "card_information")
    // private String cardInformation;

}
