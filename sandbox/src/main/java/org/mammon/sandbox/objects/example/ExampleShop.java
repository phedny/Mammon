package org.mammon.sandbox.objects.example;

import org.mammon.sandbox.generic.shop.AbstractShop;

public class ExampleShop
		extends
		AbstractShop<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	public ExampleShop(ExampleSetup setup, String identity) {
		super(setup, identity);
	}

}
