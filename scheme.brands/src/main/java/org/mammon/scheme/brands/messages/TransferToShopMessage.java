package org.mammon.scheme.brands.messages;

import org.mammon.messaging.DirectedMessage;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;

public class TransferToShopMessage implements DirectedMessage {

	private final String destination;

	private final String shop;

	@FromPersistent(TransferToShopMessage.class)
	public TransferToShopMessage(@PersistAs("destination") String destination, @PersistAs("shop") String shop) {
		this.destination = destination;
		this.shop = shop;
	}

	@Override
	public String getDestination() {
		return destination;
	}

	public String getShop() {
		return shop;
	}

}
