package com.test.authorization.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "confirmation_token")
@Data
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String token;

    private LocalDate created;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        created = LocalDate.now();
        token = UUID.randomUUID().toString();
    }

}
