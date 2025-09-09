package com.medilabo.user_ms.service;

import com.medilabo.user_ms.domain.dto.PasswordDTO;
import com.medilabo.user_ms.domain.dto.UserDTO;
import com.medilabo.user_ms.domain.entity.Patient;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.domain.enums.Role;
import com.medilabo.user_ms.repository.PatientRepository;
import com.medilabo.user_ms.repository.UserRepository;
import com.medilabo.user_ms.utils.CreatePassword;
import com.medilabo.user_ms.utils.DTOMapper;
import com.medilabo.user_ms.utils.DTOValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private DTOValidation dtoValidation;

    @Autowired
    private CreatePassword createPassword;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the connected User entity
     * @throws ResponseStatusException if authentication fails
     */
    public User getConnectedUser() {
        String email = SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getName();
        logger.info("Fetching connected user with email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    String errorMessage = "Erreur d'authentification";
                    logger.warn("Authentication failed for email: {}", email);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMessage);
                });
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user
     * @return the User entity
     * @throws ResponseStatusException if the user is not found
     */
    public User getUser(String id) {
        logger.info("Fetching user with ID: {}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "Utilisateur introuvable";
                    logger.warn("User not found with ID: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
                });
    }

    /**
     * Searches users by keyword.
     *
     * @param keyword the search keyword
     * @return a list of UserDTOs matching the keyword
     */
    public List<UserDTO> searchUsers(String keyword) {
        logger.info("Searching users with keyword: {}", keyword);
        List<User> users = userRepository.searchByKeyword(keyword);

        return users.stream()
                .map(dtoMapper::userToDTO)
                .toList();
    }

    /**
     * Creates a new user with default password.
     *
     * @param userDTO the user data transfer object
     * @param result  the binding result for validation
     * @return the created UserDTO
     */
    public UserDTO createUser(UserDTO userDTO, BindingResult result) {
        logger.info("Creating new user with email: {}", userDTO.getEmail());

        dtoValidation.validateBindingResult(result);
        dtoValidation.checkUserCreationEmailIsValid(userDTO, result);
        dtoValidation.validateBindingResult(result);

        User user = dtoMapper.dtoToUser(new User(), userDTO);
        user.setRole(Role.USER);

        String password = createPassword.createDefaultPassword(user);
        user.setPassword(passwordEncoder.encode(password));
        logger.debug("Assigned default password for user: {}", userDTO.getEmail());

        return saveUser(user);
    }

    /**
     * Updates an existing user.
     *
     * @param user    the user entity to update
     * @param userDTO the updated user data
     * @param result  the binding result for validation
     * @return the updated UserDTO
     */
    public UserDTO updateUser(User user, UserDTO userDTO, BindingResult result) {
        logger.info("Updating user with ID: {}", user.getId());

        dtoValidation.validateBindingResult(result);
        dtoValidation.checkUserUpdateEmailIsValid(userDTO, user.getEmail(), result);
        dtoValidation.validateBindingResult(result);

        user = dtoMapper.dtoToUser(user, userDTO);

        return saveUser(user);
    }

    /**
     * Updates the password of the connected user.
     *
     * @param passwordDTO the password data
     * @param result      the binding result for validation
     * @return success message in French
     */
    public String updateUserPassword(PasswordDTO passwordDTO, BindingResult result) {
        User user = getConnectedUser();
        logger.info("Updating password for user ID: {}", user.getId());

        dtoValidation.validateBindingResult(result);
        dtoValidation.checkPasswordAuth(passwordDTO, user, result);
        dtoValidation.checkPasswordFormat(passwordDTO, result);
        dtoValidation.checkConfirmPassword(passwordDTO, result);
        dtoValidation.validateBindingResult(result);

        String protectedPassword = passwordEncoder.encode(passwordDTO.getNewPassword());
        user.setPassword(protectedPassword);

        saveUser(user);
        logger.info("Password updated successfully for user ID: {}", user.getId());
        return "Mot de passe mis à jour avec succès";
    }

    /**
     * Resets the password of a user.
     *
     * @param user the user whose password is to be reset
     * @return success message with the new password in French
     */
    public String resetUserPassword(User user) {
        logger.info("Resetting password for user ID: {}", user.getId());

        String password = createPassword.createDefaultPassword(user);
        user.setPassword(passwordEncoder.encode(password));

        saveUser(user);
        logger.info("Password reset successfully for user ID: {}", user.getId());
        return "Mot de passe réinitialisé : " + password;
    }

    /**
     * Deletes a user.
     *
     * @param user the user to delete
     * @return success message in French
     * @throws ResponseStatusException if the user has patients or is an admin
     */
    public String deleteUser(User user) {
        logger.info("Deleting user with ID: {}", user.getId());

        List<Patient> patients = patientRepository.findAllByDoctorId(user.getId());

        if (!patients.isEmpty()) {
            String errorMessage = "Vous ne pouvez pas supprimer un utilisateur qui a des patients";
            logger.warn("Cannot delete user ID {}: has patients", user.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        if (user.getRole().equals(Role.ADMIN)) {
            String errorMessage = "Vous ne pouvez pas supprimer un administrateur";
            logger.warn("Cannot delete user ID {}: is an admin", user.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        try {
            userRepository.delete(user);
            logger.info("User deleted successfully with ID: {}", user.getId());
            return "Utilisateur supprimé avec succès";
        } catch (Exception e) {
            String errorMessage = "Erreur interne lors de la suppression de l'utilisateur";
            logger.error("Error deleting user ID {}: {}", user.getId(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    /**
     * Saves a user entity and returns its DTO.
     *
     * @param user the user entity to save
     * @return the saved UserDTO
     * @throws ResponseStatusException in case of internal error
     */
    public UserDTO saveUser(User user) {
        try {
            userRepository.save(user);
            logger.info("User saved successfully with ID: {}", user.getId());
            return dtoMapper.userToDTO(user);
        } catch (Exception e) {
            String errorMessage = "Erreur interne lors de la sauvegarde de l'utilisateur";
            logger.error("Error saving user ID {}: {}", user.getId(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }
}
