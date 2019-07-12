package com.vaadin.training.productdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ProductdbApplication extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(ProductdbApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProductdbApplication.class, args);
	}
}
