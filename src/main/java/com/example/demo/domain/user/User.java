package com.example.demo.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users", indexes = {@Index(name = "id_idx", columnList = "id")})
@Getter @Setter @ToString
public class User implements UserDetails {
    public static final Integer PASSWORD_EXPIRED_TERM_DAYS = 90;

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 50)
    private String id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "password_expired_dt", nullable = false)
    private LocalDate passwordExpiredDt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "expired_dt")
    private LocalDateTime expiredDt;

    @Column(name = "locked", columnDefinition = "bit default 0")
    private Boolean locked;

    @Column(name = "locked_from_dt")
    private LocalDateTime lockedFromDt;

    @Column(name = "locked_to_dt")
    private LocalDateTime lockedToDt;

    @Column(name = "created_dt", columnDefinition = "datetime default now()")
    private LocalDateTime createdDt;

    @Column(name = "modified_dt", columnDefinition = "datetime default now()")
    private LocalDateTime modifiedDt;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<UserAuthority> userAuthorities;

    public User() {
    }

    @Builder
    public User(String id, String password, LocalDate passwordExpiredDt, String name, Integer age, LocalDateTime expiredDt, List<UserAuthority> userAuthorities) {
        this.id = id;
        this.password = password;
        this.passwordExpiredDt = passwordExpiredDt;
        this.name = name;
        this.age = age;
        this.expiredDt = expiredDt;
        this.userAuthorities = userAuthorities;
    }

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
        return this.id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
