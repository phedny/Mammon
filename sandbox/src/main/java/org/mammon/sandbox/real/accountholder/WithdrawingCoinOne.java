package org.mammon.sandbox.real.accountholder;

import java.util.UUID;

import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.real.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.real.example.ExampleSetup;
import org.mammon.sandbox.real.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractWithdrawingCoinOne;
import org.mammon.scheme.brands.generic.coin.AbstractWithdrawingCoinTwo;

public class WithdrawingCoinOne
		extends
		AbstractWithdrawingCoinOne<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	private final String identity = UUID.randomUUID().toString();

	@FromPersistent(AbstractWithdrawingCoinOne.class)
	public WithdrawingCoinOne(@PersistAs("accountHolder") AbstractAccountHolderPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> accountHolder,
			@PersistAs("bank") AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank, @PersistAs("publicKey") Group.Element<Gq> publicKey,
			@PersistAs("count") int count) {
		super(accountHolder, bank, publicKey, count);
	}

	@Override
	public String getIdentity() {
		return identity.toString();
	}

	@Override
	protected AbstractWithdrawingCoinTwo<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newWithdrawingCoinTwo(
			Group.Element<Gq> a, Group.Element<Gq> b, FiniteField.Element<Z> c,
			FiniteField.Element<Z> s, FiniteField.Element<Z> x1,
			FiniteField.Element<Z> x2, FiniteField.Element<Z> u,
			FiniteField.Element<Z> v, Group.Element<Gq> bigA,
			Group.Element<Gq> bigB) {
		return new WithdrawingCoinTwo((ExampleSetup) getSetup(), (AbstractAccountHolderPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) getAccountHolder(),
				getIdentity(), (AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) getBank(), getPublicKey(), getCount(), s, x1, x2, u, v, bigA, bigB, a, b,
				c);
	}

}
