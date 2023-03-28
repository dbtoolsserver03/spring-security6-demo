package com.example.demo.domain.user;

import com.example.demo.security.user.Authority;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "user_authority")
public class UserAuthority {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority authority;

    @Column(name = "created_dt")
    private LocalDateTime createdDt;

    public UserAuthority() {
    }

    @Builder
    public UserAuthority(String userId, Authority authority, LocalDateTime createdDt) {
        this.userId = userId;
        this.authority = authority;
        this.createdDt = createdDt;
    }

}
