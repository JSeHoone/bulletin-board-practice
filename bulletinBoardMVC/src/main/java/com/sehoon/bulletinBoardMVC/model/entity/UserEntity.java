package com.sehoon.bulletinBoardMVC.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users_bulletin_board")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name= "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    public String getPassword() {
        return password;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
