package org.mammon.sandbox.messages;

import org.mammon.math.Group;
import org.mammon.messaging.Message;

public class BankWitnessesResponse<G extends Group<G>> implements Message {

	private final Group.Element<G> valA;

	private final Group.Element<G> valB;

	public BankWitnessesResponse(Group.Element<G> valA, Group.Element<G> valB) {
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
