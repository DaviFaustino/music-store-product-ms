package com.davifaustino.musicstore.products;

import org.springframework.boot.SpringApplication;

public class TestProductApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProductApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
