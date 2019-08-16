package test2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value={"nettyserver","test2"})
public class apptest2 {
	public static void main(String[] args) {
		SpringApplication.run(apptest2.class, args);
	}
}
