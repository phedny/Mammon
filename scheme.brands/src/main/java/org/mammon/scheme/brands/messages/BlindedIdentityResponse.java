package org.mammon.scheme.brands.messages;

import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Message;
import org.mammon.messaging.PersistAs;

public class BlindedIdentityResponse<G extends Group<G>> implements Message {

	private final Group.Element<G> blindedIdentity;

	@FromPersistent(Message.class)
	public BlindedIdentityResponse(@PersistAs("blindedIdentity") Group.Element<G> blindedIdentity) {
		this.blindedIdentity = blindedIdentity;
	}

	public Group.Element<G> getBlindedIdentity() {
		return blindedIdentity;
	}

}
