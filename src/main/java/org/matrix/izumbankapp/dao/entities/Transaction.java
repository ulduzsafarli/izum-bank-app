package org.matrix.izumbankapp.dao.entities;

import lombok.Getter;
import lombok.Setter;
import org.matrix.izumbankapp.dao.entities.abstractentity.Auditable;
import org.matrix.izumbankapp.enumeration.transaction.TransactionStatus;
import org.matrix.izumbankapp.enumeration.transaction.TransactionType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transactions", schema = "public")
public class Transaction extends Auditable implements Serializable {

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
    private Account account;
}