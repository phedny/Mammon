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
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBlindedIdentity;
import org.mammon.scheme.brands.generic.bank.AbstractIssuedWitnesses;

public class BlindedIdentity
		extends
		AbstractBlindedIdentity<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(AbstractBlindedIdentity.class)
	public BlindedIdentity(@PersistAs("setup") ExampleSetup setup, @PersistAs("bank") AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			@PersistAs("payerIdentity") Group.Element<Gq> payerIdentity) {
		super(setup, bank, payerIdentity);
	}

	@Override
	protected AbstractIssuedWitnesses<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newAbstractIssuedWitnesses(
			BrandsSchemeSetup<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			Group.Element<Gq> payerIdentity, FiniteField.Element<Z> w) {
		return new IssuedWitnesses((ExampleSetup) setup, (AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) bank, payerIdentity, w);
	}

}
