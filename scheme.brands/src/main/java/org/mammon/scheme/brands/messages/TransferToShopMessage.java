package org.mammon.scheme.brands.messages;

import org.mammon.messaging.DirectedMessage;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;

public class TransferToShopMessage implements DirectedMessage {

	private final String destination;

	@FromPersistent(TransferToShopMessage.class)
	public TransferToShopMessage(@PersistAs("destination") String destination) {
		this.destination = destination;
	}

	@Override
	public String getDestination() {
		return destination;
	}

}
