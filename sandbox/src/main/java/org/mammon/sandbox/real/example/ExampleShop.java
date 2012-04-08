package org.mammon.sandbox.real.example;

import org.mammon.math.Gq;
import org.mammon.math.Z;
import org.mammon.scheme.brands.generic.shop.AbstractShop;

public class ExampleShop
		extends
		AbstractShop<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	public ExampleShop(ExampleSetup setup, String identity) {
		super(setup, identity);
	}

}
