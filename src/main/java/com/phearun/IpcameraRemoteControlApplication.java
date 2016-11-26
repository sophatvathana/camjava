package com.phearun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.innodep", "com.phearun"})
public class IpcameraRemoteControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(IpcameraRemoteControlApplication.class, args);
	}
}
