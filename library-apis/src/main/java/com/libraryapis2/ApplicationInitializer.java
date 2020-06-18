package com.libraryapis2;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.libraryapis2.model.common.Gender;
import com.libraryapis2.user.UserEntity;
import com.libraryapis2.user.UserRepository;

@Component
public class ApplicationInitializer {

	BCryptPasswordEncoder bCryptPasswordEncoder;
	UserRepository userRepository;

	public ApplicationInitializer(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepository = userRepository;
	}

	@Value("${library.api.user.admin.username:lib-admin}")
	String adminUsername;

	@Value("${library.api.user.admin.password:admin-password}")
	String adminPassword;

	@PostConstruct
	private void init() {

		UserEntity admin = userRepository.findByUsername(adminUsername);

		if (admin == null) {
			admin = new UserEntity(adminUsername, bCryptPasswordEncoder.encode(adminPassword), "Library", "Admin",
					LocalDate.now().minusYears(30), Gender.Female, "000-000-000", "library.admin@library.com", "ADMIN");

			userRepository.save(admin);
		}
	}
}