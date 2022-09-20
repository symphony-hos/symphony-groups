package io.symphony.groups;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import io.symphony.common.conversion.EnableUnitConversion;
import io.symphony.common.startup.EnableSymphonyStartup;

@SpringBootApplication
@EnableMongoRepositories
@EnableUnitConversion
@EnableSymphonyStartup
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
