package com.asrez.wheremoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("package com.asrez.wheremoney.api.entity")
@SpringBootApplication
public class WhereMoneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhereMoneyApplication.class, args);
	}

}
