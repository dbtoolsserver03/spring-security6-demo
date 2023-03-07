package com.example.demo.domain.user;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    public static final Integer PASSWORD_EXPIRED_TERM_DAYS = 90;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false, unique = true)
    private String accountId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "password_expired_dt", nullable = false)
    private LocalDateTime passwordExpiredDt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "expired_dt")
    private LocalDateTime expiredDt;

    @Column(name = "locked")
    private Boolean locked;

    @Column(name = "locked_from_dt")
    private LocalDateTime lockedFromDt;

    @Column(name = "locked_to_dt")
    private LocalDateTime lockedToDt;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<UserAuthority> userAuthorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userAuthorities
                .stream()
                .map(UserAuthority::getAuthority)
                .toList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.accountId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return LocalDateTime.now().until(this.passwordExpiredDt, ChronoUnit.SECONDS) < 0;
    }

    @Override
    public boolean isEnabled() {
        return this.isAccountNonExpired() && this.isAccountNonLocked() && this.isCredentialsNonExpired();
    }
}
