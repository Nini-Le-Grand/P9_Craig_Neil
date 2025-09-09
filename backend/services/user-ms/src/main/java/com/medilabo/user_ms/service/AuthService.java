package com.medilabo.user_ms.service;

import com.medilabo.user_ms.security.JwtUtils;
import com.medilabo.user_ms.domain.dto.LoginDTO;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.repository.UserRepository;
import com.medilabo.user_ms.utils.DTOValidation;
import io.jsonwebtoken.JwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private JwtUtils jwtUtils;

    @Autowired 
    private DTOValidation dtoValidation;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LogManager.getLogger(AuthService.class);

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginDTO the login credentials
     * @param result   binding result for validation
     * @return a JWT token as a String
     * @throws ResponseStatusException if authentication fails or internal error occurs
     */
    public String login(LoginDTO loginDTO, BindingResult result) {
        logger.info("Login attempt for email: {}", loginDTO.getEmail());

        dtoValidation.validateBindingResult(result);

        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> {
                    String errorMessage = "Identifiant ou mot de passe incorrecte";
                    logger.warn("Login failed: user not found for email {}", loginDTO.getEmail());
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMessage);
                });

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            String errorMessage = "Identifiant ou mot de passe incorrecte";
            logger.warn("Login failed: incorrect password for email {}", loginDTO.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMessage);
        }

        try {
            String token = jwtUtils.generateToken(user);
            logger.info("Login successful for user ID: {}", user.getId());
            return token;
        } catch (JwtException | IllegalArgumentException e) {
            String errorMessage = "Erreur interne lors de la connexion";
            logger.error("Error generating JWT token for user ID {}: {}", user.getId(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }
}
