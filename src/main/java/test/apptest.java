package test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value={"nettyclient","test"})
public class apptest {

	public static void main(String[] args) {
		SpringApplication.run(apptest.class, args);
	}

}
