package org.mammon.sandbox;

import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;

public class SecondaryTransitionable {

	private final String primaryIdentity;

	@FromPersistent(SecondaryTransitionable.class)
	public SecondaryTransitionable(@PersistAs("primaryIdentity") String primaryIdentity) {
		this.primaryIdentity = primaryIdentity;
	}

	public String getPrimaryIdentity() {
		return primaryIdentity;
	}

}
