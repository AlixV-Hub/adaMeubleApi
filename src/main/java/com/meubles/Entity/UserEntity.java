package com.meubles.Entity;

import com.meubles.Model.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false)
    private String firstname;

    @Setter
    @Getter
    @Column(nullable = false)
    private String lastname;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Getter
    @Column(nullable = false)
    private String password;

    @Setter
    @Getter
    private String address;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "creator")
    private Set<ProductEntity> productsCreated;

    @OneToMany(mappedBy = "buyer")
    private Set<ProductEntity> productsBought;


    public UserEntity() {}

}
