package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
public class DemoApplication {

	
//	@Autowired
//    DataSource dataSource;
 

	
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

//	 @Override
//	    public void run(String... args) throws Exception {
//	        System.out.println("Our DataSource is = " + dataSource);
//	 }
	
	
}
