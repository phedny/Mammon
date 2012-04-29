package org.mammon.sandbox.objects.bank;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.objects.example.ExampleFiniteField;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBlindedIdentity;
import org.mammon.scheme.brands.generic.bank.AbstractIssuedWitnesses;

public class BlindedIdentity
		extends
		AbstractBlindedIdentity<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(AbstractBlindedIdentity.class)
	public BlindedIdentity(@PersistAs("setup") ExampleSetup setup, @PersistAs("bank") AbstractBankPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			@PersistAs("payerIdentity") Group.Element<ExampleGroup> payerIdentity) {
		super(setup, bank, payerIdentity);
	}

	@Override
	protected AbstractIssuedWitnesses<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newAbstractIssuedWitnesses(
			BrandsSchemeSetup<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			AbstractBankPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			Group.Element<ExampleGroup> payerIdentity, FiniteField.Element<ExampleFiniteField> w) {
		return new IssuedWitnesses((ExampleSetup) setup, (AbstractBankPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) bank, payerIdentity, w);
	}

}
