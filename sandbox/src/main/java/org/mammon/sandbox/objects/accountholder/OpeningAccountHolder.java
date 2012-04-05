package org.mammon.sandbox.objects.accountholder;

import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.objects.example.ExampleAccountHolder;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleFiniteField;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.generic.accountholder.AbstractOpeningAccountHolder;

public class OpeningAccountHolder
		extends
		AbstractOpeningAccountHolder<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(AbstractOpeningAccountHolder.class)
	public OpeningAccountHolder(@PersistAs("setup") ExampleSetup setup, @PersistAs("bank") ExampleBank bank) {
		super(setup, bank, setup.getFiniteField().getRandomElement());
	}

	@Override
	public String getIdentity() {
		return "opening-" + getPublicKey().toString();
	}

	@Override
	protected AccountHolderPrivate<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newAccountHolder(
			Group.Element<ExampleGroup> blindedIdentity) {
		return new ExampleAccountHolder((ExampleSetup) getSetup(), getPrivateKey(), getPublicKey(), blindedIdentity,
				(ExampleBank) getBank());
	}

}
