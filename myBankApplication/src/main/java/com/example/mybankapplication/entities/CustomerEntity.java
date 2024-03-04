package com.example.mybankapplication.entities;

import com.example.mybankapplication.annotations.AdultBirthDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "customers", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;


//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cif_sequence")
//    @SequenceGenerator(name = "cif_sequence", sequenceName = "cif_sequence", allocationSize = 1, initialValue = 1000)
//    @NotBlank(message = "CIF must not be null")
//    @Pattern(regexp = "\\d{4}", message = "CIF must consist of exactly 4 digits")
//    @Column(name = "cif", nullable = false, length = 4, unique = true)
//    private String cif;



    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private PassportEntity passport;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<AccountEntity> accounts;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<NotificationEntity> notifications;
}
