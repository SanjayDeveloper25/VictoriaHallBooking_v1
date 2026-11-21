package com.gcc.communityHall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Community Hall REST API Documentation",
				description = "GCC Community Hall REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Krish Kumari",
						email = "krishnakumari@gmail.com",
						url = "https://chennaicorporation.gov.in/gcc/"
				),
				license = @License(
						name = "Revenue Department",
						url = "https://chennaicorporation.gov.in/gcc/"
				)
		),
		externalDocs = @ExternalDocumentation(
				description =  "Gcc Community Hall REST API Documentation",
				url = "https://chennaicorporation.gov.in/gcc/"
		)
)
public class CommunityHallApplication {

	public static void main(String[] args) {
		System.out.println("community halll.......");
		SpringApplication.run(CommunityHallApplication.class, args);
	}

}
