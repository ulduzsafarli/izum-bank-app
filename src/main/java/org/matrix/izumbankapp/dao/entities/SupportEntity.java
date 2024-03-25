package org.matrix.izumbankapp.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supports", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String message;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isAnswered;
}
