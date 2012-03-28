package org.mammon.sandbox.objects.example;

import org.mammon.sandbox.OracleHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.Group.Element;

public class ExampleSignatureHashFunction extends OracleHashFunction implements SignatureHashFunction<ExampleGroup> {

	public ExampleSignatureHashFunction(ExampleGroup g) {
		super(g, 5);
	}

	@Override
	public Element<ExampleGroup> hash(Element<ExampleGroup> blindedIdentity, Element<ExampleGroup> commitment,
			Element<ExampleGroup>... secretValues) {
		return oracle(blindedIdentity, commitment, secretValues[0], secretValues[1], secretValues[2]);
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
