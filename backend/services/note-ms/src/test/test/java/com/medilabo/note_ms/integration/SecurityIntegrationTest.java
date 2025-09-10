package com.medilabo.note_ms.integration;

import com.medilabo.note_ms.utils.JwtUtils;
import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class SecurityIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JwtUtils jwtUtils;

    @Test
    void whenNoToken_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/note/someEndpoint"))
               .andExpectAll(
                       status().isUnauthorized(),
                       jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()),
                       jsonPath("$.error").value(HttpStatus.UNAUTHORIZED.name()),
                       jsonPath("$.message").value("Vous n'êtes pas authentifiés"),
                       jsonPath("$.path").value("/note/someEndpoint")
               );
    }

    @Test
    void whenInvalidToken_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/note/someEndpoint")
                                .header("Authorization", "Bearer invalid.token.here"))
               .andExpectAll(
                       status().isUnauthorized(),
                       jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()),
                       jsonPath("$.error").value(HttpStatus.UNAUTHORIZED.name()),
                       jsonPath("$.message").value("Vous n'êtes pas authentifiés"),
                       jsonPath("$.path").value("/note/someEndpoint")
               );
    }

    @Test
    void whenExpiredToken_thenUnauthorized() throws Exception {
        String token = jwtUtils.generateToken("USER", -1000L);

        mockMvc.perform(get("/note/someEndpoint")
                                .header("Authorization", "Bearer " + token))
               .andExpectAll(
                       status().isUnauthorized(),
                       jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()),
                       jsonPath("$.error").value(HttpStatus.UNAUTHORIZED.name()),
                       jsonPath("$.message").value("Vous n'êtes pas authentifiés"),
                       jsonPath("$.path").value("/note/someEndpoint")
               );
    }

    @Test
    void whenWrongRole_thenForbidden() throws Exception {
        String token = jwtUtils.generateToken("ADMIN", 36000L);

        mockMvc.perform(get("/note/someEndpoint")
                                .header("Authorization", "Bearer " + token))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()),
                       jsonPath("$.error").value(HttpStatus.FORBIDDEN.name()),
                       jsonPath("$.message").value("Vous n'êtes pas autorisés à consulter cette ressource"),
                       jsonPath("$.path").value("/note/someEndpoint")
               );
    }

    @Test
    void whenUnknownEndpoint_thenNotFound() throws Exception {
        String token = jwtUtils.generateToken("USER", 36000L);

        mockMvc.perform(get("/some/endpoint")
                                .header("Authorization", "Bearer " + token))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Route inexistante"),
                       jsonPath("$.path").value("/some/endpoint")
               );
    }
}
