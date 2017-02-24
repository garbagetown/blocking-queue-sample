package com.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ConsumeService {

	private final BlockingQueue<String> queue;
	private final QueueProperties props;
	
	@SuppressWarnings("rawtypes")
	public void execute() {
		
		int numOfThreads = props.getNumOfThreads();
		
		ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
		
		CompletableFuture[] futures = new CompletableFuture[numOfThreads];
		for (int i = 0, n = futures.length; i < n; i++) {
			futures[i] = CompletableFuture.runAsync(new Task(), executorService);
		}
		
		try {
			CompletableFuture.allOf(futures).join();
		} finally {
			executorService.shutdown();
		}
	}
	
	protected class Task implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					String s = queue.poll(props.getPollingTimeout(), TimeUnit.SECONDS);
					if (s == null) {
						break;
					}
					log.debug("consume: " + s);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
			}
		}
	}

}
