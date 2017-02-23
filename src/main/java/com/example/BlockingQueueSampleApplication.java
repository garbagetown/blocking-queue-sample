package com.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor
public class BlockingQueueSampleApplication {

	private final ProduceService produceService;
	private final ConsumeService consumeService;
	
	public static void main(String[] args) {
		SpringApplication.run(BlockingQueueSampleApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run() {
		return arg -> {
			ExecutorService executorService = Executors.newFixedThreadPool(2);
			try {
				CompletableFuture.allOf(
					CompletableFuture.runAsync(() -> produceService.execute(), executorService),
					CompletableFuture.runAsync(() -> consumeService.execute(), executorService)
				).join();
			} finally {
				executorService.shutdown();
			}
		};
	}
}
