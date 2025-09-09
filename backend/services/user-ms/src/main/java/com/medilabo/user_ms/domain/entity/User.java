package com.medilabo.user_ms.domain.entity;

import com.medilabo.user_ms.domain.enums.Gender;
import com.medilabo.user_ms.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing a user in the system.
 * Implements UserDetails for Spring Security integration.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    /**
     * Unique identifier for the user (UUID).
     */
    @Id
    @Column(columnDefinition = "CHAR(36)")
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    /**
     * User's first name.
     */
    private String firstName;

    /**
     * User's last name.
     */
    private String lastName;

    /**
     * User's date of birth.
     */
    private LocalDate dateOfBirth;

    /**
     * User's gender.
     */
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * User's unique email address (used as username).
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * User's address.
     */
    private String address;

    /**
     * User's phone number.
     */
    private String phone;

    /**
     * Encrypted user password.
     */
    private String password;

    /**
     * User role (e.g., USER or ADMIN).
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Returns authorities granted to the user for Spring Security.
     *
     * @return collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
