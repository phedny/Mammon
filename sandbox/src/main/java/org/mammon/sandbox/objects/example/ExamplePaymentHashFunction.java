package org.mammon.sandbox.objects.example;

import org.mammon.sandbox.OracleHashFunction;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.Group.Element;

public class ExamplePaymentHashFunction extends OracleHashFunction implements
		PaymentHashFunction<ExampleGroup, String, Long> {

	public ExamplePaymentHashFunction(ExampleGroup g) {
		super(g, 4);
	}

	@Override
	public Element<ExampleGroup> hash(Element<ExampleGroup> blindedIdentity, Element<ExampleGroup> commitment,
			String shopIdentity, Long time) {
		return oracle(blindedIdentity, commitment, shopIdentity, time);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ExamplePaymentHashFunction)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return ExamplePaymentHashFunction.class.getName().hashCode();
	}

	@Override
	public String toString() {
		return ExamplePaymentHashFunction.class.getSimpleName();
	}

}
