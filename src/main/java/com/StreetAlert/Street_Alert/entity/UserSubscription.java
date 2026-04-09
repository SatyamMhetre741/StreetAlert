package com.StreetAlert.Street_Alert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_subscriptions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "sector"}) // one user can't have two rows with same user_id and sector
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Article.Sector sector;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Article.ImpactLevel minImpactLevel = Article.ImpactLevel.LOW;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}