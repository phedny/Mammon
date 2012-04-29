package org.mammon.sandbox.real.bank;

import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.real.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.real.example.ExampleSetup;
import org.mammon.sandbox.real.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractIssuedWitnesses;

public class IssuedWitnesses
		extends
		AbstractIssuedWitnesses<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(AbstractIssuedWitnesses.class)
	public IssuedWitnesses(@PersistAs("setup") ExampleSetup setup, @PersistAs("bank") AbstractBankPrivate<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			@PersistAs("payerIdentity") Group.Element<Gq> payerIdentity,
			@PersistAs("w") FiniteField.Element<Z> w) {
		super(setup, bank, payerIdentity, w);
	}

}
