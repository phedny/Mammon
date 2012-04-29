package org.mammon.sandbox.real.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.real.example.ExampleAccountHolder;
import org.mammon.sandbox.real.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.real.example.ExampleSetup;
import org.mammon.sandbox.real.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.generic.accountholder.AbstractOpeningAccountHolder;
import org.mammon.scheme.brands.generic.bank.AbstractBank;

public class OpeningAccountHolder
		extends
		AbstractOpeningAccountHolder<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	public OpeningAccountHolder(@PersistAs("setup") ExampleSetup setup, @PersistAs("bank") Bank<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank) {
		super(setup, bank, setup.getFiniteField().getRandomElement());
	}

	@FromPersistent(AbstractOpeningAccountHolder.class)
	public OpeningAccountHolder(@PersistAs("setup") ExampleSetup setup, @PersistAs("bank") Bank<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			@PersistAs("privateKey") FiniteField.Element<Z> privateKey) {
		super(setup, bank, privateKey);
	}

	@Override
	protected AccountHolderPrivate<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newAccountHolder(
			Group.Element<Gq> blindedIdentity) {
		return new ExampleAccountHolder((ExampleSetup) getSetup(), getPrivateKey(), getPublicKey(), blindedIdentity,
				(AbstractBank<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) getBank());
	}

}
