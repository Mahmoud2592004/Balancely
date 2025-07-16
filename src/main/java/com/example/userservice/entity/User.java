package com.example.userservice.entity;

import com.fasterxml.jackson.databind.util.ClassUtil;
import jakarta.persistence.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = true)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = true)
    private String passwordHash;

    @Column(name = "phone_number",nullable = true)
    private String phoneNumber;

    @Column
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "location_id")
    private Integer locationId;

    public  User(){
        // Initaile value of the balance equals zerooooooooooooooooooooooooooooooooooooooooooooooo
        
        this.balance = BigDecimal.valueOf(0);
    }


    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Role getRole() {
        return role;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }
}
