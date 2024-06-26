package org.matrix.izumbankapp.dao.entities;

import lombok.Getter;
import lombok.Setter;
import org.matrix.izumbankapp.enumeration.NotificationType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "notifications", schema = "public")
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDate sentDate;
    @Enumerated(EnumType.STRING)
    private NotificationType type;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}