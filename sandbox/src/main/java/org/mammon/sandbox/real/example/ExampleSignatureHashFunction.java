package org.mammon.sandbox.real.example;

import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.sandbox.RealOracleHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;

public class ExampleSignatureHashFunction extends RealOracleHashFunction implements
		SignatureHashFunction<Gq, Z> {

	public ExampleSignatureHashFunction(Gq group, Z f) {
		super(f, 5);
	}

	@Override
	public FiniteField.Element<Z> hash(Group.Element<Gq> blindedIdentity,
			Group.Element<Gq> commitment, Group.Element<Gq> z, Group.Element<Gq> a,
			Group.Element<Gq> b) {
		return oracle(blindedIdentity, commitment, z, a, b);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ExampleSignatureHashFunction)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return ExampleSignatureHashFunction.class.getName().hashCode();
	}

	@Override
	public String toString() {
		return ExampleSignatureHashFunction.class.getSimpleName();
	}

}
