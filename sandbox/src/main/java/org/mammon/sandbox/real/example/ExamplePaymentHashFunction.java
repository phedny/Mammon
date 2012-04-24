package org.mammon.sandbox.real.example;

import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.sandbox.Sha1HashFunction;
import org.mammon.scheme.brands.PaymentHashFunction;

public class ExamplePaymentHashFunction extends Sha1HashFunction implements
		PaymentHashFunction<Gq, Z, String, Long> {

	public ExamplePaymentHashFunction(Gq group, Z f) {
		super(f, 4);
	}

	@Override
	public FiniteField.Element<Z> hash(
			Group.Element<Gq> blindedIdentity,
			Group.Element<Gq> commitment, String shopIdentity, Long time) {
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
