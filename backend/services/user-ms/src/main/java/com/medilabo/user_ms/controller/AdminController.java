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

import java.util.List;

/**
 * Controller responsible for admin operations on users.
 */
@RestController
@RequestMapping("/admin/users")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private DTOMapper dtoMapper;

    private static final Logger logger = LogManager.getLogger(AdminController.class);

    /**
     * Searches users by a keyword in first name, last name, or email.
     *
     * @param keyword search keyword
     * @return list of matching UserDTOs
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUser(@RequestParam String keyword) {
        logger.info("GET /admin/users/search - Searching users with keyword: {}", keyword);
        List<UserDTO> result = userService.searchUsers(keyword);
        logger.debug("Number of users found: {}", result.size());

        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id user ID
     * @return UserDTO of the user
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        logger.info("GET /admin/users/{} - Fetching user by ID", id);
        User user = userService.getUser(id);
        UserDTO userDTO = dtoMapper.userToDTO(user);
        logger.debug("User retrieved: {}", userDTO);

        return ResponseEntity.ok(userDTO);
    }

    /**
     * Creates a new user.
     *
     * @param userDTO user data to create
     * @param result  binding result for validation
     * @return created UserDTO
     */
    @PostMapping
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        logger.info("POST /admin/users - Creating new user: {}", userDTO);
        UserDTO createdUserDTO = userService.createUser(userDTO, result);
        logger.debug("User created: {}", createdUserDTO);

        return ResponseEntity.ok(createdUserDTO);
    }

    /**
     * Updates an existing user by ID.
     *
     * @param userDTO user data to update
     * @param result  binding result for validation
     * @param id      user ID
     * @return updated UserDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult result, @PathVariable String id) {
        logger.info("PUT /admin/users/{} - Updating user: {}", id, userDTO);
        User user = userService.getUser(id);
        UserDTO updatedUser = userService.updateUser(user, userDTO, result);
        logger.debug("User updated: {}", updatedUser);

        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Resets the password of a user by ID.
     *
     * @param id user ID
     * @return success message containing the new password
     */
    @PutMapping("/password/reset/{id}")
    public ResponseEntity<String> resetPassword(@PathVariable String id) {
        logger.info("PUT /admin/users/password/reset/{} - Resetting user password", id);
        User user = userService.getUser(id);
        String successMessage = userService.resetUserPassword(user);
        logger.debug("Password reset result: {}", successMessage);

        return ResponseEntity.ok(successMessage);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id user ID
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        logger.info("DELETE /admin/users/{} - Deleting user", id);
        User user = userService.getUser(id);
        String successMessage = userService.deleteUser(user);
        logger.debug("User deletion result: {}", successMessage);

        return ResponseEntity.ok(successMessage);
    }
}
