package org.mammon.sandbox.messages;

import org.mammon.messaging.Message;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.Group.Element;

public class BankWitnessesResponse<G extends Group<G>> implements Message {

	private final Group.Element<G>[] witness;

	public BankWitnessesResponse(Element<G>[] witness) {
		this.witness = witness;
	}

	public Group.Element<G>[] getWitness() {
		return witness;
	}

}
