package org.matrix.izumbankapp.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deposits", schema = "public")
@NamedEntityGraph(name = "Deposit.withAccount", attributeNodes = @NamedAttributeNode("account"))
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private boolean yearlyInterest;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id")
    private Account account;

}