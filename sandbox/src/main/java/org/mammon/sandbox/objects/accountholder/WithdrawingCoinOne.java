package org.mammon.sandbox.objects.accountholder;

import java.util.UUID;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.objects.example.ExampleFiniteField;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractWithdrawingCoinOne;
import org.mammon.scheme.brands.generic.coin.AbstractWithdrawingCoinTwo;

public class WithdrawingCoinOne
		extends
		AbstractWithdrawingCoinOne<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	private final String identity = UUID.randomUUID().toString();

	@FromPersistent(AbstractWithdrawingCoinOne.class)
	public WithdrawingCoinOne(@PersistAs("accountHolder") AbstractAccountHolderPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> accountHolder,
			@PersistAs("bank") AbstractBankPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank, @PersistAs("publicKey") Group.Element<ExampleGroup> publicKey,
			@PersistAs("count") int count) {
		super(accountHolder, bank, publicKey, count);
	}

	@Override
	public String getIdentity() {
		return identity.toString();
	}

	@Override
	protected AbstractWithdrawingCoinTwo<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newWithdrawingCoinTwo(
			Group.Element<ExampleGroup> a, Group.Element<ExampleGroup> b, FiniteField.Element<ExampleFiniteField> c,
			FiniteField.Element<ExampleFiniteField> s, FiniteField.Element<ExampleFiniteField> x1,
			FiniteField.Element<ExampleFiniteField> x2, FiniteField.Element<ExampleFiniteField> u,
			FiniteField.Element<ExampleFiniteField> v, Group.Element<ExampleGroup> bigA,
			Group.Element<ExampleGroup> bigB) {
		return new WithdrawingCoinTwo((ExampleSetup) getSetup(), (AbstractAccountHolderPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) getAccountHolder(),
				getIdentity(), (AbstractBankPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) getBank(), getPublicKey(), getCount(), s, x1, x2, u, v, bigA, bigB, a, b,
				c);
	}

}
