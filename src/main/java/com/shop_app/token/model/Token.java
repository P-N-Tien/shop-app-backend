package com.shop_app.token.model;


import com.shop_app.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "token_type", nullable = false, length = 20)
    private String tokenType;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "revoked", nullable = false)
    private String revoked;

    @Column(name = "expired", nullable = false)
    private String expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
