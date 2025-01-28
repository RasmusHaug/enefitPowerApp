package enefit.rasmushaug.enefitpower;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class EnefitpowerApplication {

	public static void main(String[] args) {
		Dotenv.load();
		SpringApplication.run(EnefitpowerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
