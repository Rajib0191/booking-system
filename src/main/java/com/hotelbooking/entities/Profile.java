package com.hotelbooking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(length = 100)
    private String address;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String country;

    @Column(length = 50)
    private String occupation;

    @Column(length = 20)
    private String gender;

    private String profilePictureUrl;

    @Column(updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
