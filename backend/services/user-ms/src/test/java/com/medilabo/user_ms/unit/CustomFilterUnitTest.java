package com.medilabo.user_ms.unit;

import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.repository.UserRepository;
import com.medilabo.user_ms.security.CustomFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomFilterUnitTest {

    @Mock
    private UserRepository userRepository;

    private TestableCustomFilter filter;

    private static final String SECRET_KEY = "thisisakeythathasexactly32char!!";

    @BeforeEach
    void setup() throws Exception {
        filter = new TestableCustomFilter();

        Field secretKeyField = CustomFilter.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(filter, SECRET_KEY);

        Field userRepoField = CustomFilter.class.getDeclaredField("userRepository");
        userRepoField.setAccessible(true);
        userRepoField.set(filter, userRepository);

        SecurityContextHolder.clearContext();
    }

    private String generateToken(String userId, long validityMillis) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                   .subject(userId)
                   .claim("role", "USER")
                   .issuedAt(new Date(now))
                   .expiration(new Date(now + validityMillis))
                   .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                   .compact();
    }

    @Test
    void whenValidTokenAndUserExists_thenAuthenticationSet() throws Exception {
        String token = generateToken("u1", 3600000);

        User user = new User();
        user.setId("u1");
        user.setEmail("test@example.com");
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        filter.doFilterInternalPublic(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth, "Authentication should be set");
        assertEquals(user, auth.getPrincipal(), "Principal should be the User entity");
        assertTrue(auth.getAuthorities().stream()
                       .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void whenTokenValidButUserNotFound_thenNoAuthentication() throws Exception {
        String token = generateToken("unknown", 3600000);
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        filter.doFilterInternalPublic(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth, "Authentication should be null if user not found");

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void whenExpiredToken_thenNoAuthentication() throws Exception {
        String token = generateToken("u1", -1000);

        User user = new User();
        user.setId("u1");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        filter.doFilterInternalPublic(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth, "Authentication should be null for expired token");

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void whenNoToken_thenNoAuthentication() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/test");
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternalPublic(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth, "Authentication should be null when no token present");

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void whenInvalidToken_thenNoAuthentication() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/test");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");

        filter.doFilterInternalPublic(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth, "Authentication should be null for invalid token");

        verify(chain, times(1)).doFilter(request, response);
    }

    static class TestableCustomFilter extends CustomFilter {
        public void doFilterInternalPublic(HttpServletRequest request,
                HttpServletResponse response,
                FilterChain chain) throws Exception {
            super.doFilterInternal(request, response, chain);
        }
    }
}
