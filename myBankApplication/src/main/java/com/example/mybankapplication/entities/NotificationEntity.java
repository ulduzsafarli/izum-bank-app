package com.example.mybankapplication.entities;

import com.example.mybankapplication.enumeration.NotificationType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "notifications", schema = "public")
@Data
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private boolean read;
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;
    @Enumerated(EnumType.STRING)
    private NotificationType type;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

}