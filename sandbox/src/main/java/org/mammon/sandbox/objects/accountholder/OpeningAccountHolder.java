package org.mammon.sandbox.objects.accountholder;

import org.mammon.sandbox.generic.accountholder.AbstractOpeningAccountHolder;
import org.mammon.sandbox.objects.example.ExampleAccountHolder;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.Group.Element;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;

public class OpeningAccountHolder
		extends
		AbstractOpeningAccountHolder<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction, String> {

	public OpeningAccountHolder(ExampleSetup setup, ExampleBank bank) {
		super(setup, bank, setup.getGroup().getRandomElement(null));
	}

	@Override
	public String getIdentity() {
		return "opening-" + getPublicKey().toString();
	}

	@Override
	protected AccountHolderPrivate<ExampleGroup, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newAccountHolder(
			Element<ExampleGroup> blindedIdentity) {
		return new ExampleAccountHolder((ExampleSetup) getSetup(), getPrivateKey(), getPublicKey(), blindedIdentity,
				(ExampleBank) getBank());
	}

}
