package com.backendtravelbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories
public class BackendTravelboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendTravelboxApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer corsconfigurer (){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings (CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:4200")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedHeaders("Authorization", "Content-Type")
						.allowCredentials(true);
			}
		};
	}
}
