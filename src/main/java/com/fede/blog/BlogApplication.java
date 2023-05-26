package com.fede.blog;

import com.fede.blog.model.Role;
import com.fede.blog.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import static com.fede.blog.model.ERole.*;

@SpringBootApplication
@EnableAsync
@Slf4j
@EnableWebSocketMessageBroker
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}


    //for dev use only
	@Bean
	CommandLineRunner populateRoleRepo(RoleRepository roleRepository) {
		return args -> {
			if (!roleRepository.findAll().isEmpty()) {
				BlogApplication.log.info("Skipping db roles insertion.");
			} else {
				log.info("Inserting roles...");
				roleRepository.saveAndFlush(new Role(ROLE_USER));
				roleRepository.saveAndFlush(new Role(ROLE_ADMIN));
				roleRepository.saveAndFlush(new Role(ROLE_MODERATOR));
				log.info("Roles inserted.");
			}
		};
	}

}
