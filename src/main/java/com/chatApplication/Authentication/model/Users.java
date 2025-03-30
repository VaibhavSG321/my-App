package com.chatApplication.Authentication.model;



import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(unique = true,nullable = false)
    private String uniqueID;
    public Users(){

    }
    //Only for Redis
    public Users(String username, String role, String uniqueID) {
        this.username = username;
        this.role = role;
        this.uniqueID = uniqueID;
    }
    //Only for Postgre
    public Users(String username, String password, String role, String uniqueID) {
        this.username = username;
        this.password = password;
        this.setRole(role);
        this.uniqueID = uniqueID;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (!role.startsWith("ROLE_")) {
            this.role = "ROLE_" + role;
        } else {
            this.role = role;
        }
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", uniqueID='" + uniqueID + '\'' +
                '}';
    }
}
