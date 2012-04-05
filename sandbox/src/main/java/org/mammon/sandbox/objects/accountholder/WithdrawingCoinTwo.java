package org.mammon.sandbox.objects.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.objects.example.ExampleAccountHolder;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleCoinSignature;
import org.mammon.sandbox.objects.example.ExampleFiniteField;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.sandbox.objects.example.ExampleUnspentCoin;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.generic.coin.AbstractCoinSignature;
import org.mammon.scheme.brands.generic.coin.AbstractWithdrawingCoinTwo;

public class WithdrawingCoinTwo
		extends
		AbstractWithdrawingCoinTwo<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	private final String identity;

	@FromPersistent(AbstractWithdrawingCoinTwo.class)
	public WithdrawingCoinTwo(@PersistAs("setup") ExampleSetup setup,
			@PersistAs("accountHolder") ExampleAccountHolder accountHolder, @PersistAs("identity") String identity,
			@PersistAs("bank") ExampleBank bank, @PersistAs("publicKey") Group.Element<ExampleGroup> publicKey,
			@PersistAs("count") int count, @PersistAs("blindingFactor") FiniteField.Element<ExampleFiniteField> s,
			@PersistAs("x1") FiniteField.Element<ExampleFiniteField> x1,
			@PersistAs("x2") FiniteField.Element<ExampleFiniteField> x2,
			@PersistAs("u") FiniteField.Element<ExampleFiniteField> u,
			@PersistAs("v") FiniteField.Element<ExampleFiniteField> v,
			@PersistAs("bigA") Group.Element<ExampleGroup> bigA, @PersistAs("bigB") Group.Element<ExampleGroup> bigB,
			@PersistAs("a") Group.Element<ExampleGroup> a, @PersistAs("b") Group.Element<ExampleGroup> b,
			@PersistAs("c") FiniteField.Element<ExampleFiniteField> c) {
		super(setup, accountHolder, bank, publicKey, count, s, x1, x2, u, v, bigA, bigB, a, b, c);
		this.identity = identity;
	}

	@Override
	public String getIdentity() {
		return identity.toString();
	}

	@Override
	protected ExampleUnspentCoin newUnspentCoin(FiniteField.Element<ExampleFiniteField> r,
			CoinSignature<ExampleGroup, ExampleFiniteField> coinSignature) {
		return new ExampleUnspentCoin((ExampleSetup) getSetup(), (ExampleAccountHolder) getAccountHolder(),
				(ExampleBank) getBank(), identity, getBlindingFactor(), getX1(), getX2(), getBigA(), getBigB(), r,
				coinSignature);
	}

	@Override
	protected AbstractCoinSignature<ExampleGroup, ExampleFiniteField> newCoinSignature(Element<ExampleGroup> z,
			Element<ExampleGroup> a, Element<ExampleGroup> b, org.mammon.math.FiniteField.Element<ExampleFiniteField> r) {
		return new ExampleCoinSignature(z, a, b, r);
	}

}
