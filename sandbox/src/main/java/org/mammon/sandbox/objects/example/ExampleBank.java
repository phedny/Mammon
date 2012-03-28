package org.mammon.sandbox.objects.example;

import org.mammon.sandbox.generic.AbstractBankPrivate;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.Group.Element;

public class ExampleBank
		extends
		AbstractBankPrivate<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> {

	public ExampleBank(
			BrandsSchemeSetup<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			Element<ExampleGroup> privateKey) {
		super(setup, privateKey);
	}

	@Override
	public String getIdentity() {
		return "bank-" + getPublicKey();
	}
}
