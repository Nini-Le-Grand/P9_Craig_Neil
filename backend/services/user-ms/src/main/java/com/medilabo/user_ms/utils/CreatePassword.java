package com.medilabo.user_ms.utils;

import com.medilabo.user_ms.domain.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Utility service for creating default passwords for users.
 */
@Service
public class CreatePassword {

    private static final Logger logger = LogManager.getLogger(CreatePassword.class);

    /**
     * Creates a default password for a user.
     * The password format is: First 3 letters of last name (capitalized) + year of birth + "!".
     * If the last name is shorter than 3 letters, it is padded with 'z'.
     *
     * @param user The user entity
     * @return Generated default password
     */
    public String createDefaultPassword(User user) {
        logger.info("Generating default password for user ID {}", user.getId());

        StringBuilder namePart = new StringBuilder(user.getLastName());
        while (namePart.length() < 3) {
            namePart.append('z');
        }

        String formattedName = Character.toUpperCase(namePart.charAt(0)) +
                namePart.substring(1, 3).toLowerCase();
        int year = user.getDateOfBirth().getYear();
        String password = formattedName + year + "!";

        logger.info("Default password generated for user ID {}", user.getId());
        return password;
    }
}
