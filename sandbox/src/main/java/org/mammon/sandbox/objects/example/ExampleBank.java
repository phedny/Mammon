package org.mammon.sandbox.objects.example;

import org.mammon.math.FiniteField;
import org.mammon.sandbox.generic.AbstractBankPrivate;
import org.mammon.scheme.brands.BrandsSchemeSetup;

public class ExampleBank
		extends
		AbstractBankPrivate<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	public ExampleBank(
			BrandsSchemeSetup<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			FiniteField.Element<ExampleFiniteField> privateKey) {
		super(setup, privateKey);
	}

	@Override
	public String getIdentity() {
		return "bank-" + getPublicKey();
	}
}
