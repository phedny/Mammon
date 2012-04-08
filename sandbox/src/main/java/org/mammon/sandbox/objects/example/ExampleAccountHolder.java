package org.mammon.sandbox.objects.example;

import org.mammon.Bearer;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.objects.accountholder.WithdrawingCoinOne;
import org.mammon.scheme.brands.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractWithdrawingCoinOne;
import org.mammon.scheme.brands.messages.ObtainCoinsMessage;

public class ExampleAccountHolder
		extends
		AbstractAccountHolderPrivate<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(Bearer.class)
	public ExampleAccountHolder(@PersistAs("setup") ExampleSetup setup,
			@PersistAs("privateKey") FiniteField.Element<ExampleFiniteField> privateKey,
			@PersistAs("publicKey") Group.Element<ExampleGroup> publicKey,
			@PersistAs("blindedIdentity") Group.Element<ExampleGroup> blindedIdentity,
			@PersistAs("bank") AbstractBankPrivate<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank) {
		super(setup, privateKey, publicKey, blindedIdentity, bank);
	}

	@Override
	protected AbstractWithdrawingCoinOne<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newWithdrawingCoinOne(
			ObtainCoinsMessage request) {
		return new WithdrawingCoinOne(this, (AbstractBankPrivate<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) getBank(), getPublicKey(), request.getCount());
	}

}
