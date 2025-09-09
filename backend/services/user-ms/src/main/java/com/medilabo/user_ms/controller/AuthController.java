package com.medilabo.user_ms.controller;

import com.medilabo.user_ms.domain.dto.LoginDTO;
import com.medilabo.user_ms.domain.dto.PasswordDTO;
import com.medilabo.user_ms.service.AuthService;
import com.medilabo.user_ms.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller responsible for handling authentication and password update endpoints.
 */
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    /**
     * Authenticates a user and retrieves a JWT token.
     *
     * @param loginDTO login credentials
     * @param result   binding result for validation
     * @return ResponseEntity containing a map with the JWT token
     */
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult result) {
        logger.info("POST /auth/login - Attempting login for email: {}", loginDTO.getEmail());

        String token = authService.login(loginDTO, result);
        logger.info("POST /auth/login - Login successful, token generated");

        return ResponseEntity.ok(Map.of("token", token));
    }

    /**
     * Updates the password of the currently connected user.
     *
     * @param passwordDTO password update data
     * @param result      binding result for validation
     * @return ResponseEntity containing a success message
     */
    @PutMapping("/password/update")
    public ResponseEntity<String> editUserPassword(@Valid @RequestBody PasswordDTO passwordDTO, BindingResult result) {
        logger.info("PUT /password/update - Updating password for connected user");

        String responseMessage = userService.updateUserPassword(passwordDTO, result);
        logger.info("PUT /password/update - Password update successful");

        return ResponseEntity.ok(responseMessage);
    }
}
