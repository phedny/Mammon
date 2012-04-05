package org.mammon.scheme.brands.messages;

import org.mammon.math.Group;
import org.mammon.messaging.DirectedMessage;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Message;
import org.mammon.messaging.PersistAs;

public class BlindedIdentityRequest<G extends Group<G>> implements DirectedMessage {

	private final String destination;

	private final Group.Element<G> identity;

	@FromPersistent(Message.class)
	public BlindedIdentityRequest(@PersistAs("destination") String destination,
			@PersistAs("identity") Group.Element<G> identity) {
		this.destination = destination;
		this.identity = identity;
	}

	public Group.Element<G> getIdentity() {
		return identity;
	}

	@Override
	public String getDestination() {
		return destination;
	}

}
