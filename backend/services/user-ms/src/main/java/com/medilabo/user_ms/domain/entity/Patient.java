package com.medilabo.user_ms.domain.entity;

import com.medilabo.user_ms.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity representing a patient in the system.
 */
@Entity
@Table(name = "patients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

    /**
     * Unique identifier for the patient (UUID).
     */
    @Id
    @Column(columnDefinition = "CHAR(36)")
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    /**
     * Patient's first name.
     */
    private String firstName;

    /**
     * Patient's last name.
     */
    private String lastName;

    /**
     * Patient's date of birth.
     */
    private LocalDate dateOfBirth;

    /**
     * Patient's gender.
     */
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * Patient's unique email address.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Patient's address.
     */
    private String address;

    /**
     * Patient's phone number.
     */
    private String phone;

    /**
     * Doctor assigned to this patient.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;
}
