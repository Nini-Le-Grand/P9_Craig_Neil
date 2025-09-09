package com.medilabo.evaluation_ms.unit;

import com.medilabo.evaluation_ms.security.CustomFilter;
import com.medilabo.evaluation_ms.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomFilterTest {
    @Autowired private JwtUtils jwtUtils;
    private TestableCustomFilter filter;
    private static final String SECRET_KEY = "thisisakeythathasexactly32char!!";
    private static final long EXPIRATION_TIME = 3600000L;

    @BeforeEach
    void setup() {
        filter = new TestableCustomFilter();
        SecurityContextHolder.clearContext();
    }

    @Test
    void whenValidToken_thenSecurityContextUpdated() throws Exception {
        String token = jwtUtils.generateToken("USER", EXPIRATION_TIME);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        request.addHeader("Authorization", "Bearer " + token);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        filter.doFilterInternalPublic(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth, "Authentication should not be null");
        assertEquals(123L, auth.getPrincipal(), "Principal should match userId");
        assertTrue(auth.getAuthorities().stream()
                       .anyMatch(a -> a.getAuthority().equals("ROLE_USER")), "Authority ROLE_USER should be present");

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void whenAdminToken_thenSecurityContextUpdated() throws Exception {
        String token = jwtUtils.generateToken("ADMIN", EXPIRATION_TIME);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        request.addHeader("Authorization", "Bearer " + token);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        filter.doFilterInternalPublic(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth, "Authentication should not be null");
        assertEquals(123L, auth.getPrincipal(), "Principal should match userId");
        assertTrue(auth.getAuthorities().stream()
                       .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")), "Authority ROLE_ADMIN should be present");

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void whenNoToken_thenSecurityContextEmpty() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        filter.doFilterInternalPublic(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth, "Authentication should be null when no token");

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void whenInvalidToken_thenSecurityContextEmpty() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        request.addHeader("Authorization", "Bearer invalid.token.here");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        filter.doFilterInternalPublic(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth, "Authentication should be null for invalid token");

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void whenTokenExpired_thenSecurityContextEmpty() throws Exception {
        String token = jwtUtils.generateToken("USER", -1000L);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        request.addHeader("Authorization", "Bearer " + token);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        filter.doFilterInternalPublic(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth, "Authentication should be null for expired token");

        verify(chain, times(1)).doFilter(request, response);
    }

    static class TestableCustomFilter extends CustomFilter {
        public TestableCustomFilter() {
            super.secretKey = SECRET_KEY;
        }

        public void doFilterInternalPublic(jakarta.servlet.http.HttpServletRequest request,
                jakarta.servlet.http.HttpServletResponse response,
                FilterChain chain) throws Exception {
            super.doFilterInternal(request, response, chain);
        }
    }
}
