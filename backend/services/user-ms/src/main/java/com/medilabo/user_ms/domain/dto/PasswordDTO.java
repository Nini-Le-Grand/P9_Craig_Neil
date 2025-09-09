package com.medilabo.user_ms.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Data Transfer Object for updating a user's password.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordDTO {

    /**
     * Current password of the user.
     * Must not be blank.
     */
    @NotBlank(message = "Le mot de passe actuel est obligatoire")
    private String currentPassword;

    /**
     * New password to set.
     * Must not be blank.
     */
    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    private String newPassword;

    /**
     * Confirmation of the new password.
     * Must not be blank and should match the new password.
     */
    @NotBlank(message = "La confirmation du nouveau mot de passe est obligatoire")
    private String confirmPassword;
}
