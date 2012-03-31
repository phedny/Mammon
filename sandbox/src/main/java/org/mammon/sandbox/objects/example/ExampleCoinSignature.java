package org.mammon.sandbox.objects.example;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.scheme.brands.generic.coin.AbstractCoinSignature;

public class ExampleCoinSignature extends AbstractCoinSignature<ExampleGroup, ExampleFiniteField> {

	public ExampleCoinSignature(Group.Element<ExampleGroup> z, Group.Element<ExampleGroup> a,
			Group.Element<ExampleGroup> b, FiniteField.Element<ExampleFiniteField> r) {
		super(z, a, b, r);
	}

}
