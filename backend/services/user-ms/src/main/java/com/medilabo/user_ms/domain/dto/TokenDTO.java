package com.medilabo.user_ms.domain.dto;

import lombok.*;

/**
 * Data Transfer Object representing a JWT token response.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDTO {

    /**
     * JWT token string.
     */
    private String token;
}
