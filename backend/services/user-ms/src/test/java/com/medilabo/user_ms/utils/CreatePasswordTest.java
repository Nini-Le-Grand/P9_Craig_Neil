/*
package com.medilabo.users.utils;

import com.medilabo.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreatePasswordTest {

    private CreatePassword createPassword;

    @BeforeEach
    void setUp() {
        createPassword = new CreatePassword();
    }

    @Test
    void createDefaultPassword_shouldGenerateCorrectPassword_withNormalLastName() {
        User user = new User();
        user.setLastName("smith");
        user.setDateOfBirth(LocalDate.of(1990, 5, 10));

        String password = createPassword.createDefaultPassword(user);
        assertEquals("Smi1990!", password);
    }

    @Test
    void createDefaultPassword_shouldPadLastName_withLessThan3Letters() {
        User user = new User();
        user.setLastName("Li"); // < 3 letters
        user.setDateOfBirth(LocalDate.of(1985, 12, 25));

        String password = createPassword.createDefaultPassword(user);
        assertEquals("Liz1985!", password);
    }

    @Test
    void createDefaultPassword_shouldUseFirst3Letters_withExactly3Letters() {
        User user = new User();
        user.setLastName("Kim");
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));

        String password = createPassword.createDefaultPassword(user);
        assertEquals("Kim2000!", password);
    }

    @Test
    void createDefaultPassword_shouldCapitalizeFirstLetter() {
        User user = new User();
        user.setLastName("roberts");
        user.setDateOfBirth(LocalDate.of(1995, 8, 15));

        String password = createPassword.createDefaultPassword(user);
        assertEquals("Rob1995!", password);
    }

    @Test
    void createDefaultPassword_shouldCapitalizeFirstLetterOnly() {
        User user = new User();
        user.setLastName("ROberts");
        user.setDateOfBirth(LocalDate.of(1995, 8, 15));

        String password = createPassword.createDefaultPassword(user);
        assertEquals("Rob1995!", password);
    }
}
*/
