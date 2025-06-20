package com.hotelbooking.dtos;

import com.hotelbooking.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    @NotBlank(message = "Firstname is required!")
    private String firstName;

    @NotBlank(message = "Lastname is required!")
    private String lastName;

    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "Password is required!")
    private String password;

    @NotBlank(message = "Phone number is required!")
    private String phoneNumber;

    private UserRole role;
}
