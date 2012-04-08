package org.mammon.sandbox.objects.example;

import org.mammon.Issuer;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.objects.bank.BlindedIdentity;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBlindedIdentity;

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
	protected AbstractBlindedIdentity<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newBlindedIdentity(
			BrandsSchemeSetup<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			AbstractBankPrivate<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			Group.Element<ExampleGroup> payerIdentity) {
		return new BlindedIdentity((ExampleSetup) setup, (AbstractBankPrivate<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) bank, payerIdentity);
	}

	@Override
	protected String getIdentityForPayerIdentity(Element<ExampleGroup> payerIdentity) {
		return "blindedidentity-" + getIdentity() + "-" + payerIdentity;
	}
}
