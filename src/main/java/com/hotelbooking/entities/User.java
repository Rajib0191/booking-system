package com.hotelbooking.entities;

import com.hotelbooking.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email is required!")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required!")
    private String password;
    private String firstName;
    private String lastName;

    @NotBlank(message = "Phone Number is required!")
    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Boolean isActive;
    private final LocalDateTime createdAt = LocalDateTime.now();

//    private LocalDateTime createdAt;
//
//    @PrePersist
//    public void prePersist() {
//        if (createdAt == null) {
//            createdAt = LocalDateTime.now();
//        }
//    }

}
