package br.edu.ifpb.kodak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// @SpringBootApplication
@SpringBootApplication(exclude = SecurityAutoConfiguration.class) // ignore security bcz its bothersome
public class KodakApplication {

	public static void main(String[] args) {
		SpringApplication.run(KodakApplication.class, args);
	}

}
