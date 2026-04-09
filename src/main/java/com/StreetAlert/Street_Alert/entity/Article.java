package com.StreetAlert.Street_Alert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(nullable = false, unique = true, length = 32)
    private String urlHash;

    private String source;

    @Enumerated(EnumType.STRING)
    private Sector sector;

    @Enumerated(EnumType.STRING)
    private ImpactLevel impactLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleStatus status = ArticleStatus.UNPROCESSED;

    private LocalDateTime publishedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fetchedAt = LocalDateTime.now();

    public enum Sector {
        BANKING, IT, CRYPTO, ENERGY, GENERAL
    }

    public enum ImpactLevel {
        HIGH, MEDIUM, LOW
    }

    public enum ArticleStatus {
        UNPROCESSED, PROCESSED, FAILED
    }
}