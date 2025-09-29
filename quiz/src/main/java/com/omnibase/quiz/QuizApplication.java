package com.omnibase.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@SpringBootApplication
@EnableTransactionManagement
public class QuizApplication {

	public static void main(String[] args) {

		TimeZone tz = TimeZone.getTimeZone("Asia/Kolkata");
		TimeZone.setDefault(tz);
		System.setProperty("user.timezone", "Asia/Kolkata");

		SpringApplication.run(QuizApplication.class, args);
	}

}
