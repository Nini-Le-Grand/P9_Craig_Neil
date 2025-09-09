package com.medilabo.user_ms.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Data Transfer Object for user login credentials.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {

    /**
     * User's email address.
     * Must not be blank and must be a valid email format.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email
    private String email;

    /**
     * User's password.
     * Must not be blank.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
