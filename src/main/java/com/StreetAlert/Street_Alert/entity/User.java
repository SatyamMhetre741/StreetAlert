package com.StreetAlert.Street_Alert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "user_sectors",                     // This is the join table name
            joinColumns = @JoinColumn(name = "user_id"),      // Foreign key to User
            inverseJoinColumns = @JoinColumn(name = "sector_id")  // Foreign key to Sector
    )
    private List<Sector> sectors = new ArrayList<>();

    // Helper methods
    public void addSector(Sector sector) {
        this.sectors.add(sector);
        sector.getUsers().add(this);     // maintain both sides
    }

    public void removeSector(Sector sector) {
        this.sectors.remove(sector);
        sector.getUsers().remove(this);
    }

    public enum Role {
        USER, ADMIN
    }
}