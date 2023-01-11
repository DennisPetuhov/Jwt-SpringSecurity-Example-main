package com.example.springsecurityexample.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NaturalId;

@Entity// ассоциируем класс с таблицей в Базе Данных.
// класс соответствует таблице, обьект класса соответствует одной сроке таблицы
@Table(name = "users", uniqueConstraints = { // аннотация @Table используется когда нужны собственные настройки для создания таблицы
        @UniqueConstraint(columnNames = { // поле Username должно быть уникальным
            "username"
        }),
        @UniqueConstraint(columnNames = { // поле email должно быть уникальным
            "email"
        })
})
public class User{
	@Id //поле id будет гарантировать уникальность обьекта
    @GeneratedValue(strategy = GenerationType.IDENTITY) // тип генерации первичного ключа
    private Long id;

    @NotBlank
    @Size(min=3, max = 50)
    private String name;

    @NotBlank
    @Size(min=3, max = 50)
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min=6, max = 100)
    private String password;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private UserProfile userProfile;

    @ManyToMany(fetch = FetchType.LAZY)//
    @JoinTable(name = "user_roles", // создаем таблицу для связи user_roles
    	joinColumns = @JoinColumn(name = "user_id"), //создаем в таблице Колонка user_id(которое буде ассоциироваться с обьектом USER)
    	inverseJoinColumns = @JoinColumn(name = "role_id")) // создается колонка role_id которая будет ассоциироваться с обьектом Role
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}