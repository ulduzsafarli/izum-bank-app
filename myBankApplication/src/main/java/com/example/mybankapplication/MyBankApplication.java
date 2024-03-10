package com.example.mybankapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBankApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService service
//	) {
//		return args -> {
//			var admin = RegisterRequest.builder()
//
//					.phoneNumber("0703472190")
//					.email("admin@mail.com")
//					.password("password")
//					.roles(Collections.singleton(ADMIN))
//					.build();
//			System.out.println("Admin token: " + service.register(admin).getToken());
//		};
//	}
}
