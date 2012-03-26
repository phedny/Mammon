package org.mammon.brands.example;

import org.mammon.brands.PaymentHashFunction;
import org.mammon.brands.Group.Element;

public class ExamplePaymentHashFunction implements PaymentHashFunction<ExampleGroup, String, Long> {

	@Override
	public Element<ExampleGroup> hash(Element<ExampleGroup> blindedIdentity, Element<ExampleGroup> commitment,
			String shopIdentity, Long time) {
		return null;
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
