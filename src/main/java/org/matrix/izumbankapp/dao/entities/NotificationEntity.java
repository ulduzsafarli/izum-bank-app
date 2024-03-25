package org.matrix.izumbankapp.dao.entities;

import org.matrix.izumbankapp.enumeration.NotificationType;
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
    @JoinColumn(name = "user_id")
    private UserEntity user;

}