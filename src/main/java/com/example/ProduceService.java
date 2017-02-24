package com.example;

import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ProduceService {

	private final BlockingQueue<String> queue;
	
	public void execute() {
		IntStream.range(0, 100)
			.parallel()
			.boxed()
			.map(i -> String.format("%03d", i))
			.forEach(s -> {
				try {
					queue.put(s);
					log.debug("produce: " + s);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
			});
	}

}
