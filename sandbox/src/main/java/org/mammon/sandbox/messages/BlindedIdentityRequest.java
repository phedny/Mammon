package org.mammon.sandbox.messages;

import org.mammon.brands.Group;
import org.mammon.messaging.DirectedMessage;

public class BlindedIdentityRequest<G extends Group<G>, I> implements DirectedMessage<I> {

	private final I destination;

	private final Group.Element<G> identity;

	public BlindedIdentityRequest(I destination, Group.Element<G> identity) {
		this.destination = destination;
		this.identity = identity;
	}

	public Group.Element<G> getIdentity() {
		return identity;
	}

	@Override
	public I getDestination() {
		return destination;
	}

}
