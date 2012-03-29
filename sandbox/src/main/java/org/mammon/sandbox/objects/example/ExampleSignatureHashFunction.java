package org.mammon.sandbox.objects.example;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.sandbox.OracleHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;

public class ExampleSignatureHashFunction extends OracleHashFunction implements
		SignatureHashFunction<ExampleGroup, ExampleFiniteField> {

	public ExampleSignatureHashFunction(ExampleGroup group, ExampleFiniteField f) {
		super(f, 5);
	}

	@Override
	public FiniteField.Element<ExampleFiniteField> hash(
			Group.Element<ExampleGroup> blindedIdentity,
			Group.Element<ExampleGroup> commitment,
			Group.Element<ExampleGroup>... secretValues) {
		return oracle(blindedIdentity, commitment, secretValues[0],
				secretValues[1], secretValues[2]);
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
