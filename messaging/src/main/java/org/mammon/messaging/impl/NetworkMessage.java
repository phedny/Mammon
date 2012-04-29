package org.mammon.messaging.impl;

import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Message;
import org.mammon.messaging.PersistAs;

public class NetworkMessage {

	private final String source;

	private final String destination;

	private final Message message;

	@FromPersistent(NetworkMessage.class)
	public NetworkMessage(@PersistAs("source") String source, @PersistAs("destination") String destination,
			@PersistAs("message") Message message) {
		this.source = source;
		this.destination = destination;
		this.message = message;
	}

	public String getSource() {
		return source;
	}

	public String getDestination() {
		return destination;
	}

	public Message getMessage() {
		return message;
	}

}
