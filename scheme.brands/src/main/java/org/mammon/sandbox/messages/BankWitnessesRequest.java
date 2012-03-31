package org.mammon.sandbox.messages;

import org.mammon.math.Group;
import org.mammon.messaging.DirectedMessage;

public class BankWitnessesRequest<G extends Group<G>, I> implements DirectedMessage<I> {

	private final I destination;

	private final Group.Element<G> identity;

	private final int count;

	public BankWitnessesRequest(I destination, Group.Element<G> identity, int count) {
		this.destination = destination;
		this.identity = identity;
		this.count = count;
	}

	public Group.Element<G> getIdentity() {
		return identity;
	}

	public int getCount() {
		return count;
	}

	@Override
	public I getDestination() {
		return destination;
	}

}
