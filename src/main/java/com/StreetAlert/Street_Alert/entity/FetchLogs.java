package com.StreetAlert.Street_Alert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "fetch_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FetchLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fetchedAt = LocalDateTime.now();

    private Integer articlesFetched = 0;

    private Integer articlesSaved = 0;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}