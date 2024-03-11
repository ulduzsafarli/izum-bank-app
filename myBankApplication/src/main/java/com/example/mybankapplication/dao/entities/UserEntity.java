package com.example.mybankapplication.dao.entities;

import com.example.mybankapplication.dao.entities.abstractentity.Auditable;
import com.example.mybankapplication.enumeration.auth.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserEntity extends Auditable implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 4)
    private String password;

    @Column(name = "cif", length = 5)
    private String cif;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_profile_id", referencedColumnName = "userProfileId")
//    private UserProfileEntity userProfile;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "user_profile_id", referencedColumnName = "userProfileId")
    private UserProfileEntity userProfile;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<AccountEntity> accounts;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<NotificationEntity> notifications;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
