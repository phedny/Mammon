package org.mammon.sandbox.messages;

import org.mammon.messaging.DirectedMessage;

public class ObtainCoinsMessage<I> implements DirectedMessage<I> {

	private final I destination;

	private final int count;

	public ObtainCoinsMessage(I destination, int count) {
		this.destination = destination;
		this.count = count;
	}

	public I getDestination() {
		return destination;
	}

	public int getCount() {
		return count;
	}

}
