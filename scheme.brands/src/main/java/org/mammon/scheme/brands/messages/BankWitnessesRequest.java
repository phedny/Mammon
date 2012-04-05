package org.mammon.scheme.brands.messages;

import org.mammon.math.Group;
import org.mammon.messaging.DirectedMessage;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Message;
import org.mammon.messaging.PersistAs;

public class BankWitnessesRequest<G extends Group<G>> implements DirectedMessage {

	private final String destination;

	private final Group.Element<G> identity;

	private final int count;

	@FromPersistent(Message.class)
	public BankWitnessesRequest(@PersistAs("destination") String destination,
			@PersistAs("identity") Group.Element<G> identity, @PersistAs("count") int count) {
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
	public String getDestination() {
		return destination;
	}

}
