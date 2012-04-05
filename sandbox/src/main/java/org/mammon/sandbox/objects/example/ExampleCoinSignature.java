package org.mammon.sandbox.objects.example;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.generic.coin.AbstractCoinSignature;

public class ExampleCoinSignature extends AbstractCoinSignature<ExampleGroup, ExampleFiniteField> {

	@FromPersistent(CoinSignature.class)
	public ExampleCoinSignature(@PersistAs("valZ") Group.Element<ExampleGroup> z,
			@PersistAs("valZ") Group.Element<ExampleGroup> a, @PersistAs("valB") Group.Element<ExampleGroup> b,
			@PersistAs("valR") FiniteField.Element<ExampleFiniteField> r) {
		super(z, a, b, r);
	}

}
