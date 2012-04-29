package org.mammon.sandbox.objects.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.objects.example.ExampleAccountHolder;
import org.mammon.sandbox.objects.example.ExampleFiniteField;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.generic.accountholder.AbstractOpeningAccountHolder;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;

public class OpeningAccountHolder
		extends
		AbstractOpeningAccountHolder<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	public OpeningAccountHolder(@PersistAs("setup") ExampleSetup setup, @PersistAs("bank") AbstractBankPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank) {
		super(setup, bank, setup.getFiniteField().getRandomElement());
	}

	@FromPersistent(AbstractOpeningAccountHolder.class)
	public OpeningAccountHolder(@PersistAs("setup") ExampleSetup setup, @PersistAs("bank") AbstractBankPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			@PersistAs("privateKey") FiniteField.Element<ExampleFiniteField> privateKey) {
		super(setup, bank, privateKey);
	}

	@Override
	protected AccountHolderPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newAccountHolder(
			Group.Element<ExampleGroup> blindedIdentity) {
		return new ExampleAccountHolder((ExampleSetup) getSetup(), getPrivateKey(), getPublicKey(), blindedIdentity,
				(AbstractBankPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) getBank());
	}

}
