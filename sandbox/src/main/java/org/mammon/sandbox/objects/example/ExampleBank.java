package org.mammon.sandbox.objects.example;

import org.mammon.Issuer;
import org.mammon.math.FiniteField;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;

public class ExampleBank
		extends
		AbstractBankPrivate<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(Issuer.class)
	public ExampleBank(
			@PersistAs("setup") BrandsSchemeSetup<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			@PersistAs("privateKey") FiniteField.Element<ExampleFiniteField> privateKey) {
		super(setup, privateKey);
	}

	@Override
	public String getIdentity() {
		return "bank-" + getPublicKey();
	}
}
