package com.example.mybankapplication.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "passports", schema = "public")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PassportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    @Column(name = "personal_no", nullable = false, unique = true)
    private String personalNo;
    @Column(name = "expired_date", nullable = false)
    private LocalDate expiredDate;

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private UserEntity user;
}