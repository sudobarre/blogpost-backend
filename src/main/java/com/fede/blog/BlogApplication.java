package com.fede.blog;

import com.fede.blog.model.ERole;
import com.fede.blog.model.Role;
import com.fede.blog.model.User;
import com.fede.blog.repository.RoleRepository;
import com.fede.blog.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import static com.fede.blog.model.ERole.*;

@SpringBootApplication
@EnableAsync
@Slf4j
public class BlogApplication {

	@Autowired
	private Environment env;
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
				log.info("Inserting roles");
				roleRepository.saveAndFlush(new Role(ROLE_USER));
				roleRepository.saveAndFlush(new Role(ROLE_ADMIN));
				roleRepository.saveAndFlush(new Role(ROLE_MODERATOR));
			}
		};
	}

/*
	@Bean
	CommandLineRunner logSecrets() {
		return args -> {
			log.info("DB_HOST: {}", env.getProperty("DB_HOST"));
			log.info("DB_NAME: {}", env.getProperty("DB_NAME"));
			log.info("DB_USERNAME: {}", env.getProperty("DB_USERNAME"));
			log.info("DB_PASSWORD: {}", env.getProperty("DB_PASSWORD"));
			log.info("DB_PORT: {}", env.getProperty("DB_PORT"));
			log.info("JWT_EXPIRATION: {}", env.getProperty("JWT_EXPIRATION"));
			log.info("JWT_REFRESH_EXPIRATION: {}", env.getProperty("JWT_REFRESH_EXPIRATION"));
			log.info("JWT_SECRET: {}", env.getProperty("JWT_SECRET"));
			log.info("MAIL_USERNAME: {}", env.getProperty("MAIL_USERNAME"));
			log.info("MAIL_PASSWORD: {}", env.getProperty("MAIL_PASSWORD"));
			log.info("MAIL_HOST: {}", env.getProperty("MAIL_HOST"));
		};
	}

 */


}
