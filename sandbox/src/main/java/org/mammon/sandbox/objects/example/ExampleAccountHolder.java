package org.mammon.sandbox.objects.example;

import org.mammon.sandbox.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.sandbox.generic.coin.AbstractWithdrawingCoinOne;
import org.mammon.sandbox.messages.ObtainCoinsMessage;
import org.mammon.sandbox.objects.accountholder.WithdrawingCoinOne;
import org.mammon.scheme.brands.Group.Element;

public class ExampleAccountHolder
		extends
		AbstractAccountHolderPrivate<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> {

	public ExampleAccountHolder(ExampleSetup setup, Element<ExampleGroup> privateKey, Element<ExampleGroup> publicKey,
			Element<ExampleGroup> blindedIdentity, ExampleBank bank) {
		super(setup, privateKey, publicKey, blindedIdentity, bank);
	}

	@Override
	public String getIdentity() {
		return "accountholder-" + getBlindedIdentity();
	}

	@Override
	protected AbstractWithdrawingCoinOne<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> newWithdrawingCoinOne(
			ObtainCoinsMessage<String> request) {
		return new WithdrawingCoinOne(this, (ExampleBank) getBank(), getPublicKey(), request.getCount());
	}

}
