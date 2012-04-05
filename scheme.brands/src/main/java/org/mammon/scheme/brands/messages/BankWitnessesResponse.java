package org.mammon.scheme.brands.messages;

import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Message;
import org.mammon.messaging.PersistAs;

public class BankWitnessesResponse<G extends Group<G>> implements Message {

	private final Group.Element<G> valA;

	private final Group.Element<G> valB;

	@FromPersistent(Message.class)
	public BankWitnessesResponse(@PersistAs("valA") Group.Element<G> valA, @PersistAs("valB") Group.Element<G> valB) {
		this.valA = valA;
		this.valB = valB;
	}

	public Group.Element<G> getValA() {
		return valA;
	}

	public Group.Element<G> getValB() {
		return valB;
	}

}
