package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "otps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Otp {
    /*
    mermaid
    erDiagram
        OTP {
            string phoneNumber PK
            string code
            timestamp expiresAt
        }
    */
    @Id
    private String phoneNumber;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Instant expiresAt;
}