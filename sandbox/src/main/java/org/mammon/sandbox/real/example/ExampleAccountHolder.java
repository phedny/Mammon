package org.mammon.sandbox.real.example;

import java.util.UUID;

import org.mammon.Bearer;
import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.real.accountholder.WithdrawingCoinOne;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBank;
import org.mammon.scheme.brands.generic.coin.AbstractWithdrawingCoinOne;
import org.mammon.scheme.brands.messages.ObtainCoinsMessage;

public class ExampleAccountHolder extends
		AbstractAccountHolderPrivate<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(Bearer.class)
	public ExampleAccountHolder(
			@PersistAs("setup") BrandsSchemeSetup<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			@PersistAs("privateKey") FiniteField.Element<Z> privateKey,
			@PersistAs("publicKey") Group.Element<Gq> publicKey,
			@PersistAs("blindedIdentity") Group.Element<Gq> blindedIdentity,
			@PersistAs("bank") Bank<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank) {
		super(setup, privateKey, publicKey, blindedIdentity, bank);
	}

	@Override
	protected AbstractWithdrawingCoinOne<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newWithdrawingCoinOne(
			ObtainCoinsMessage request) {
		return new WithdrawingCoinOne(
				this,
				(AbstractBank<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) getBank(),
				getPublicKey(), request.getCount(), UUID.randomUUID().toString());
	}

}
