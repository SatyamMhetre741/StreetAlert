package com.StreetAlert.Street_Alert.repository;

import com.StreetAlert.Street_Alert.entity.KeywordRule;
import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface KeywordRuleRepository extends JpaRepository<KeywordRule, Long> {
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT NOW()")
    LocalDateTime createdAt = LocalDateTime.now();

    List<KeywordRule> findAllByOrderByCreatedAtDesc();
}
