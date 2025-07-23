package com.example.repository;

import com.example.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface IOtpRepository extends JpaRepository<Otp, String> {
    
    @Query("SELECT o FROM Otp o WHERE o.phoneNumber = :phoneNumber AND o.code = :code AND o.expiresAt > :now")
    Optional<Otp> findValidOtp(@Param("phoneNumber") String phoneNumber, @Param("code") String code, @Param("now") Instant now);
}