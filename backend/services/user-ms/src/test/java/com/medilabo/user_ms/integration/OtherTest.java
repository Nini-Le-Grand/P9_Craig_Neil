package com.medilabo.user_ms.integration;

import com.medilabo.user_ms.domain.enums.Gender;
import com.medilabo.user_ms.domain.enums.Role;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootTest
public class OtherTest {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void login_shouldReturnToken_whenValidCredentials() throws Exception {
        User user1 = User.builder()
                .email("user1@test.com")
                .firstName("User")
                .lastName("One")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender(Gender.M)
                .role(Role.USER)
                .password(passwordEncoder.encode("Valid123!"))
                .build();
        System.out.println(user1.getId());
        userRepository.save(user1);

        User user2 = User.builder()
                            .email("user2@test.com")
                            .firstName("User")
                            .lastName("Two")
                            .dateOfBirth(LocalDate.of(1990, 1, 1))
                            .gender(Gender.F)
                            .role(Role.ADMIN)
                            .password(passwordEncoder.encode("Valid123!"))
                            .build();
        userRepository.save(user2);
    }
}
