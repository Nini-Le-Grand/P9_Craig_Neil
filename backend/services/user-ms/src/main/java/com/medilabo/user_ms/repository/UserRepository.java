package com.medilabo.user_ms.repository;

import com.medilabo.user_ms.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Finds a user by their email address.
     *
     * @param email the user's email
     * @return an Optional containing the User if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Searches users by a keyword in first name, last name, or email (case-insensitive).
     *
     * @param keyword the search keyword
     * @return list of users matching the keyword
     */
    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchByKeyword(@Param("keyword") String keyword);
}
