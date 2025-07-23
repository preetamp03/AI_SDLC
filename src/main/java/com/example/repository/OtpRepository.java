package com.example.repository;

import com.example.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {
    /**
     * Finds an OTP by phone number.
     * @param phoneNumber The user's phone number.
     * @return An optional containing the OTP if it exists.
     */
    Optional<Otp> findByPhoneNumber(String phoneNumber);
}