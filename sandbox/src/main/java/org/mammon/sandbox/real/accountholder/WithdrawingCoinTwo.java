package org.mammon.sandbox.real.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.math.Group.Element;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.real.example.ExampleCoinSignature;
import org.mammon.sandbox.real.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.real.example.ExampleSetup;
import org.mammon.sandbox.real.example.ExampleSignatureHashFunction;
import org.mammon.sandbox.real.example.ExampleUnspentCoin;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractCoinSignature;
import org.mammon.scheme.brands.generic.coin.AbstractWithdrawingCoinTwo;

public class WithdrawingCoinTwo
		extends
		AbstractWithdrawingCoinTwo<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(AbstractWithdrawingCoinTwo.class)
	public WithdrawingCoinTwo(
			@PersistAs("setup") ExampleSetup setup,
			@PersistAs("accountHolder") AbstractAccountHolderPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> accountHolder,
			@PersistAs("identity") String identity,
			@PersistAs("bank") AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			@PersistAs("publicKey") Group.Element<Gq> publicKey, @PersistAs("count") int count,
			@PersistAs("blindingFactor") FiniteField.Element<Z> s,
			@PersistAs("x1") FiniteField.Element<Z> x1,
			@PersistAs("x2") FiniteField.Element<Z> x2,
			@PersistAs("u") FiniteField.Element<Z> u,
			@PersistAs("v") FiniteField.Element<Z> v,
			@PersistAs("bigA") Group.Element<Gq> bigA, @PersistAs("bigB") Group.Element<Gq> bigB,
			@PersistAs("a") Group.Element<Gq> a, @PersistAs("b") Group.Element<Gq> b,
			@PersistAs("c") FiniteField.Element<Z> c) {
		super(setup, accountHolder, identity, bank, publicKey, count, s, x1, x2, u, v, bigA, bigB, a, b, c);
	}

	@Override
	protected ExampleUnspentCoin newUnspentCoin(FiniteField.Element<Z> r,
			CoinSignature<Gq, Z> coinSignature) {
		return new ExampleUnspentCoin(
				(ExampleSetup) getSetup(),
				(AbstractAccountHolderPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) getAccountHolder(),
				(AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) getBank(),
				identity, getBlindingFactor(), getX1(), getX2(), getBigA(), getBigB(), r, coinSignature);
	}

	@Override
	protected AbstractCoinSignature<Gq, Z> newCoinSignature(Element<Gq> z,
			Element<Gq> a, Element<Gq> b, org.mammon.math.FiniteField.Element<Z> r) {
		return new ExampleCoinSignature(z, a, b, r);
	}

}
