package org.mammon.sandbox.objects.example;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.sandbox.objects.accountholder.WithdrawingCoinOne;
import org.mammon.scheme.brands.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractWithdrawingCoinOne;
import org.mammon.scheme.brands.messages.ObtainCoinsMessage;

public class ExampleAccountHolder
		extends
		AbstractAccountHolderPrivate<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	public ExampleAccountHolder(ExampleSetup setup,
			FiniteField.Element<ExampleFiniteField> privateKey,
			Group.Element<ExampleGroup> publicKey,
			Group.Element<ExampleGroup> blindedIdentity, ExampleBank bank) {
		super(setup, privateKey, publicKey, blindedIdentity, bank);
	}

	@Override
	public String getIdentity() {
		return "accountholder-" + getBlindedIdentity();
	}

	@Override
	protected AbstractWithdrawingCoinOne<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newWithdrawingCoinOne(
			ObtainCoinsMessage<String> request) {
		return new WithdrawingCoinOne(this, (ExampleBank) getBank(),
				getPublicKey(), request.getCount());
	}

}
