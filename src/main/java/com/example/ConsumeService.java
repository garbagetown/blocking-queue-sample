package com.example;

import java.util.concurrent.BlockingQueue;
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
	
	public void execute() {
		ExecutorService service = Executors.newFixedThreadPool(props.getNumOfThreads());
		try {
			service.execute(new Task());
		} finally {
			service.shutdown();
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
