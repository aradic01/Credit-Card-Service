package hr.rba.creditcardservice;

import hr.rba.creditcardservice.security.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.*;

import java.util.concurrent.*;

@SpringBootApplication
public class CreditCardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditCardServiceApplication.class, args);
	}

}
