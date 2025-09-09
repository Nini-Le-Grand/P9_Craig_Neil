package com.medilabo.gateway.security;

/**
 * Defines the roles used in the application for Spring Security.
 * <p>
 * This class contains only constants representing roles: USER and ADMIN.
 * It is a utility class and cannot be instantiated.
 */
public final class Roles {

    /** Standard role for regular users */
    public static final String USER = "USER";

    /** Role for administrators */
    public static final String ADMIN = "ADMIN";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Roles() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
