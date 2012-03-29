package org.mammon.sandbox.messages;

import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.messaging.Message;

public class BlindedIdentityResponse<G extends Group<G>> implements Message {

	private final Group.Element<G> blindedIdentity;

	public BlindedIdentityResponse(Element<G> blindedIdentity) {
		this.blindedIdentity = blindedIdentity;
	}

	public Group.Element<G> getBlindedIdentity() {
		return blindedIdentity;
	}

}
