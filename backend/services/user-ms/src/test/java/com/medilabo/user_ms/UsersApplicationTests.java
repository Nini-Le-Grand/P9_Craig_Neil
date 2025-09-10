package com.medilabo.user_ms;

import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")

class UsersApplicationTests {
	@Autowired
	private UserRepository userRepository;

	@Value("${spring.datasource.url}")
	private String datasourceUrl;

	@Test
	void checkDatasource() {
		System.out.println(">>> Datasource: " + datasourceUrl);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testSaveAndFindUser() {
		User user = new User();
		user.setEmail("alice@test.com");
		user.setPassword("password123");

		userRepository.save(user);

		Optional<User> found = userRepository.findByEmail("alice@test.com");
		assertTrue(found.isPresent());
		assertEquals("alice@test.com", found.get().getEmail());
	}

}
