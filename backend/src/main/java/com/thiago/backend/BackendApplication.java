package com.thiago.backend;

import com.thiago.backend.api.entity.User;
import com.thiago.backend.api.enums.ProfileEnum;
import com.thiago.backend.api.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	// Aciona o método que inicializa o banco com um usuário admin
	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUser(userRepository, passwordEncoder);
		};
	}

	// Sobe a aplicação inserindo um usuário no banco de dados com o intuito de
	// testar o login
	private void initUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		User admin = new User();
		admin.setEmail("admin@helpdesk.com");
		admin.setPassword(passwordEncoder.encode("123456"));
		admin.setProfile(ProfileEnum.ROLE_ADMIN);

		User find = userRepository.findByEmail("admin@helpdesk.com");
		if (find == null) {
			userRepository.save(admin);
		}
	}

}
