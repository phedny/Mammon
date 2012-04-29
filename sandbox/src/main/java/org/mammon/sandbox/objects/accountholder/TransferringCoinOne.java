package org.mammon.sandbox.objects.accountholder;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.objects.example.ExampleCoinSignature;
import org.mammon.sandbox.objects.example.ExampleFiniteField;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.scheme.brands.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractTransferringCoinOne;

public class TransferringCoinOne
		extends
		AbstractTransferringCoinOne<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(AbstractTransferringCoinOne.class)
	public TransferringCoinOne(
			@PersistAs("setup") ExampleSetup setup,
			@PersistAs("bank") AbstractBankPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			@PersistAs("bearer") AbstractAccountHolderPrivate<ExampleGroup, ExampleFiniteField, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bearer,
			@PersistAs("blindedIdentity") Group.Element<ExampleGroup> blindedIdentity,
			@PersistAs("commitment") Group.Element<ExampleGroup> commitment,
			@PersistAs("coinSignature") ExampleCoinSignature coinSignature,
			@PersistAs("assetType") AssetType assetType, @PersistAs("faceValue") Number faceValue,
			@PersistAs("s") FiniteField.Element<ExampleFiniteField> s,
			@PersistAs("x1") FiniteField.Element<ExampleFiniteField> x1,
			@PersistAs("x2") FiniteField.Element<ExampleFiniteField> x2, @PersistAs("identity") String identity,
			@PersistAs("shop") String shop) {
		super(setup, bank, bearer, blindedIdentity, commitment, coinSignature, assetType, faceValue, s, x1, x2,
				identity, shop);
	}

}
