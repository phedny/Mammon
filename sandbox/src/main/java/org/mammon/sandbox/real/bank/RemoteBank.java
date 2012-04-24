package org.mammon.sandbox.real.bank;

import org.mammon.Issuer;
import org.mammon.math.Gq;
import org.mammon.math.Z;
import org.mammon.math.Gq.GqElement;
import org.mammon.messaging.ForwardRemote;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.real.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.real.example.ExampleSetup;
import org.mammon.sandbox.real.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.generic.bank.AbstractBank;

@ForwardRemote
public class RemoteBank extends
		AbstractBank<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> implements
		Identifiable {

	private final String identity;

	@FromPersistent(Issuer.class)
	public RemoteBank(@PersistAs("setup") ExampleSetup setup, @PersistAs("publicKey") GqElement publicKey,
			@PersistAs("identity") String identity) {
		super(setup, publicKey);
		this.identity = identity;
	}

	@Override
	public String getIdentity() {
		return identity;
	}
}
