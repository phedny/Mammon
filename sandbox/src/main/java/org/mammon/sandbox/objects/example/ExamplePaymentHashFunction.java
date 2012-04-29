package org.mammon.sandbox.objects.example;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.sandbox.OracleHashFunction;
import org.mammon.scheme.brands.PaymentHashFunction;

public class ExamplePaymentHashFunction extends OracleHashFunction implements
		PaymentHashFunction<ExampleGroup, ExampleFiniteField, Long> {

	public ExamplePaymentHashFunction(ExampleGroup group, ExampleFiniteField f) {
		super(f, 4);
	}

	@Override
	public FiniteField.Element<ExampleFiniteField> hash(
			Group.Element<ExampleGroup> blindedIdentity,
			Group.Element<ExampleGroup> commitment, String shopIdentity, Long time) {
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
