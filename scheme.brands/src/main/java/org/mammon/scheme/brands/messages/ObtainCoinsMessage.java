package org.mammon.scheme.brands.messages;

import org.mammon.messaging.DirectedMessage;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Message;
import org.mammon.messaging.PersistAs;

public class ObtainCoinsMessage implements DirectedMessage {

	private final String destination;

	private final int count;

	@FromPersistent(Message.class)
	public ObtainCoinsMessage(@PersistAs("destination") String destination, @PersistAs("count") int count) {
		this.destination = destination;
		this.count = count;
	}

	public String getDestination() {
		return destination;
	}

	public int getCount() {
		return count;
	}

}
