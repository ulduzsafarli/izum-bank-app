package com.example.mybankapplication;

import com.example.mybankapplication.model.auth.RegisterRequest;
import com.example.mybankapplication.service.auth.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

import static com.example.mybankapplication.enumeration.auth.Role.ADMIN;

@SpringBootApplication
public class MyBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBankApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.fin("59EHLWF")
					.phoneNumber("0703472190")
					.email("admin@mail.com")
					.password("password")
					.roles(Collections.singleton(ADMIN))
					.build();
			System.out.println("Admin token: " + service.register(admin).getToken());
		};
	}
}
