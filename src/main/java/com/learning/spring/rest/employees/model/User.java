package com.learning.spring.rest.employees.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "userType")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private User.Gender sex;
    private Long phoneNumber;
    private String email;
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "roleId")}
    )
    private Set<Role> roles;

    public enum Gender {
        M, F;
    }

    public User(Integer userId, String firstName, String lastName, Gender sex, Long phoneNumber, String email, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }
}


