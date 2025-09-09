package com.medilabo.user_ms.controller;

import com.medilabo.user_ms.domain.dto.UserDTO;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.service.UserService;
import com.medilabo.user_ms.utils.DTOMapper;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling user-related endpoints.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DTOMapper dtoMapper;

    private static final Logger logger = LogManager.getLogger(UserController.class);

    /**
     * Retrieves the currently connected user's profile.
     *
     * @return ResponseEntity containing the UserDTO of the connected user.
     */
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getConnectedUser() {
        logger.info("GET /user/profile - Fetching connected user");

        User user = userService.getConnectedUser();
        logger.debug("Connected user retrieved: {}", user.getId());

        UserDTO userDto = dtoMapper.userToDTO(user);
        logger.debug("Mapped user to DTO: {}", userDto);

        logger.info("GET /user/profile - Returning user DTO");
        return ResponseEntity.ok(userDto);
    }

    /**
     * Updates the profile of the currently connected user.
     *
     * @param userDTO the new user data to update
     * @param result  binding result for validation
     * @return ResponseEntity containing the updated UserDTO
     */
    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateUserProfile(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        logger.info("PUT /user/profile - Updating profile for connected user");

        User user = userService.getConnectedUser();
        logger.debug("Connected user retrieved: {}", user.getId());

        UserDTO updatedProfile = userService.updateUser(user, userDTO, result);
        logger.debug("Updated user profile: {}", updatedProfile);

        logger.info("PUT /user/profile - Returning updated user DTO");
        return ResponseEntity.ok(updatedProfile);
    }
}
