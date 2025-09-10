package com.medilabo.user_ms.unit;

import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.utils.CreatePassword;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreatePasswordUnitTest {

    private final CreatePassword createPassword = new CreatePassword();

    @Test
    void testCreateDefaultPassword_normalCase() {
        User user = new User();
        user.setId("u1");
        user.setLastName("Dupont");
        user.setDateOfBirth(LocalDate.of(1990, 5, 12));

        String password = createPassword.createDefaultPassword(user);

        assertEquals("Dup1990!", password);
    }

    @Test
    void testCreateDefaultPassword_lastNameTooShort_oneLetter() {
        User user = new User();
        user.setId("u2");
        user.setLastName("A");
        user.setDateOfBirth(LocalDate.of(1985, 3, 20));

        String password = createPassword.createDefaultPassword(user);

        assertEquals("Azz1985!", password);
    }

    @Test
    void testCreateDefaultPassword_lastNameTooShort_twoLetters() {
        User user = new User();
        user.setId("u3");
        user.setLastName("Li");
        user.setDateOfBirth(LocalDate.of(2000, 7, 15));

        String password = createPassword.createDefaultPassword(user);

        assertEquals("Liz2000!", password);
    }

    @Test
    void testCreateDefaultPassword_exactlyThreeLetters() {
        User user = new User();
        user.setId("u4");
        user.setLastName("Sam");
        user.setDateOfBirth(LocalDate.of(1975, 1, 1));

        String password = createPassword.createDefaultPassword(user);

        assertEquals("Sam1975!", password);
    }

    @Test
    void testCreateDefaultPassword_mixedCaseLastName() {
        User user = new User();
        user.setId("u5");
        user.setLastName("mARTin");
        user.setDateOfBirth(LocalDate.of(1995, 12, 25));

        String password = createPassword.createDefaultPassword(user);

        assertEquals("Mar1995!", password);
    }
}
