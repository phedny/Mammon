package org.mammon.sandbox.messages;

import org.mammon.brands.Group;
import org.mammon.brands.Group.Element;
import org.mammon.messaging.Message;

public class BankWitnessesResponse<G extends Group<G>> implements Message {

	private final Group.Element<G>[] witness;

	public BankWitnessesResponse(Element<G>[] witness) {
		this.witness = witness;
	}

	public Group.Element<G>[] getWitness() {
		return witness;
	}

}
