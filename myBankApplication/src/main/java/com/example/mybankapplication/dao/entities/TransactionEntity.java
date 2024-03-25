package com.example.mybankapplication.dao.entities;

import com.example.mybankapplication.dao.entities.abstractentity.Auditable;
import com.example.mybankapplication.enumeration.transaction.TransactionStatus;
import com.example.mybankapplication.enumeration.transaction.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transactions", schema = "public")
public class TransactionEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Column(name = "transaction_uuid",unique = true)
    private String transactionUUID;
    private String comments;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;


    // Процентная ставка: для сберегательных счетов
    // @Column(name = "interest_rate")
    // private BigDecimal interestRate;


    // Limit поля: для определенных счетов, лимит на сумму, которую можно снять
    // @Column(name = "withdrawal_limit")
    // private BigDecimal withdrawalLimit;

}
