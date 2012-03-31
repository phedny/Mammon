package org.mammon.sandbox.objects.example;

import org.mammon.math.FiniteField;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;

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
