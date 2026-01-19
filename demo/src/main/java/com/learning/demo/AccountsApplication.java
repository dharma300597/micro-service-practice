package com.learning.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Account MicroService CRUD end-points",
                description = "Account MicroService create,update,fetch and delete account's data end-points",
                contact = @Contact(
                        name = "dharma",
                        email = "dharmalingam609@gmail.com",
                        url = "http://localhost:8085/swagger-ui/index.html"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.google.com"                )
        )
)
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
