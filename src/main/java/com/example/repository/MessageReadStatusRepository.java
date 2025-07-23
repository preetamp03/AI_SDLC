package com.example.repository;

import com.example.model.MessageReadStatus;
import com.example.model.MessageReadStatusId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatus, MessageReadStatusId> {
}