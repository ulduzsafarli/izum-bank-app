package org.matrix.izumbankapp.dao.entities;

import org.matrix.izumbankapp.dao.entities.abstractentity.Auditable;
import org.matrix.izumbankapp.enumeration.auth.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "users", schema = "public")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends Auditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_profile_id", referencedColumnName = "user_profile_id")
    private UserProfileEntity userProfile;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private List<AccountEntity> accounts;

    public UserEntity(Long id) {
        super();
        this.id = id;
    }

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
