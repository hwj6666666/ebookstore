package com.reins.bookstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

@Entity
@Data
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NaturalId
    String username;
    String password;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id")
    User user;

    Integer identity;

    public Long getUserId() {
        return user.getId();
    }
}
