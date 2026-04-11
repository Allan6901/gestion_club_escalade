package myapp;

import myapp.ioc.IHello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication()
@ServletComponentScan
public class MyApp implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MyApp.class, args);
	}

	@Autowired
    IHello helloService;
	@Override
	public void run(String... args) throws Exception {
		helloService.hello();
	}

}

