package com.example.demo.domain.user;

import com.example.demo.security.Authority;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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

    @Column(name = "created_by")
    private String createdBy;

}
