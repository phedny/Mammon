package org.mammon.sandbox.real.example;

import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.generic.coin.AbstractCoinSignature;

public class ExampleCoinSignature extends AbstractCoinSignature<Gq, Z> {

	@FromPersistent(CoinSignature.class)
	public ExampleCoinSignature(@PersistAs("valZ") Group.Element<Gq> z,
			@PersistAs("valZ") Group.Element<Gq> a, @PersistAs("valB") Group.Element<Gq> b,
			@PersistAs("valR") FiniteField.Element<Z> r) {
		super(z, a, b, r);
	}

}
