package com.StreetAlert.Street_Alert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "keyword_rules")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Article.Sector sector;

    @Column(nullable = false, length = 100)
    private String keyword;

    @Column(nullable = false)
    private Integer impactScore = 1;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}