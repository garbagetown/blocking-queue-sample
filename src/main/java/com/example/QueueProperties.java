package com.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "blocking.queue")
@Data
public class QueueProperties {

	private int numOfThreads;
	private int pollingTimeout;
	
	@Bean
	public BlockingQueue<String> queue() {
		return new ArrayBlockingQueue<>(numOfThreads);
	}
}
