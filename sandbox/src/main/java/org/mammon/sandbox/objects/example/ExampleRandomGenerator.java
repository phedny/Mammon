package org.mammon.sandbox.objects.example;

import java.util.concurrent.atomic.AtomicInteger;

public class ExampleRandomGenerator {

	private final AtomicInteger nextRandom = new AtomicInteger();

	public String nextString() {
		int id = nextRandom.getAndIncrement();
		StringBuffer value = new StringBuffer();
		do {
			value.append((char) ('a' + id % 26));
			id /= 26;
		} while (id >= 26);
		return value.reverse().toString();
	}
}
