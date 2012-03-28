package org.mammon.sandbox.messages;

import org.mammon.messaging.Message;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.Group.Element;

public class BlindedIdentityResponse<G extends Group<G>> implements Message {

	private final Group.Element<G> blindedIdentity;

	public BlindedIdentityResponse(Element<G> blindedIdentity) {
		this.blindedIdentity = blindedIdentity;
	}

	public Group.Element<G> getBlindedIdentity() {
		return blindedIdentity;
	}

}
