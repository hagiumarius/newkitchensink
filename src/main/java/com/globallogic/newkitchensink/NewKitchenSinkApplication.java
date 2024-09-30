package com.globallogic.newkitchensink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.globallogic.newkitchensink")
@EnableJpaRepositories("com.globallogic.newkitchensink.repository")
public class NewKitchenSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewKitchenSinkApplication.class, args);
	}

}
