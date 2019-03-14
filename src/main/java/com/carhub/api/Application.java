package com.carhub.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
@EnableJpaRepositories(basePackages = "com.carhub.api.auth.repositories", repositoryFactoryBeanClass = JpaRepositoryFactoryBean.class)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
