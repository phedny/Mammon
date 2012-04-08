package org.mammon.sandbox.real.accountholder;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.real.example.ExampleCoinSignature;
import org.mammon.sandbox.real.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.real.example.ExampleSetup;
import org.mammon.sandbox.real.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractTransferringCoinOne;

public class TransferringCoinOne
		extends
		AbstractTransferringCoinOne<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(AbstractTransferringCoinOne.class)
	public TransferringCoinOne(@PersistAs("setup") ExampleSetup setup, @PersistAs("bank") AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			@PersistAs("bearer") AbstractAccountHolderPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bearer,
			@PersistAs("blindedIdentity") Group.Element<Gq> blindedIdentity,
			@PersistAs("commitment") Group.Element<Gq> commitment,
			@PersistAs("coinSignature") ExampleCoinSignature coinSignature,
			@PersistAs("assetType") AssetType assetType, @PersistAs("faceValue") Number faceValue,
			@PersistAs("s") FiniteField.Element<Z> s,
			@PersistAs("x1") FiniteField.Element<Z> x1,
			@PersistAs("x2") FiniteField.Element<Z> x2, @PersistAs("identity") String identity) {
		super(setup, bank, bearer, blindedIdentity, commitment, coinSignature, assetType, faceValue, s, x1, x2,
				identity);
	}

}
