package org.mammon.sandbox.real.example;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.real.accountholder.TransferringCoinOne;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.coin.UnspentCoin;
import org.mammon.scheme.brands.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractTransferringCoinOne;
import org.mammon.scheme.brands.generic.coin.AbstractUnspentCoin;

public class ExampleUnspentCoin extends
		AbstractUnspentCoin<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(UnspentCoin.class)
	public ExampleUnspentCoin(
			@PersistAs("setup") ExampleSetup setup,
			@PersistAs("bearer") AbstractAccountHolderPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bearer,
			@PersistAs("issuer") AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			@PersistAs("identity") String identity,
			@PersistAs("dualIdentity") String dualIdentity,
			@PersistAs("blindingFactor") FiniteField.Element<Z> blindingFactor,
			@PersistAs("payerWitness1") FiniteField.Element<Z> x1,
			@PersistAs("payerWitness2") FiniteField.Element<Z> x2,
			@PersistAs("blindedIdentity") Group.Element<Gq> blindedIdentity,
			@PersistAs("commitment") Group.Element<Gq> commitment, @PersistAs("r") FiniteField.Element<Z> r,
			@PersistAs("coinSignature") CoinSignature<Gq, Z> coinSignature) {
		super(setup, bearer, bank, identity, dualIdentity, blindingFactor, x1, x2, blindedIdentity,
				commitment, r, coinSignature);
	}

	@Override
	public String toString() {
		return "I, " + getIssuer() + ", owe the bearer of this IOU, " + getBearer() + ", the repayment of "
				+ getAssetType().getCallSign() + " " + getFaceValue() + " [BF=" + getBlindingFactor() + ", PW=("
				+ getPayerWitness1() + "," + getPayerWitness2() + "), COIN=(" + getBlindedIdentity() + ","
				+ getCommitment() + ",(" + getCoinSignature().getValZ() + "," + getCoinSignature().getValA() + ","
				+ getCoinSignature().getValB() + "," + getCoinSignature().getValR() + "))]";
	}

	public String getDualIdentity() {
		return getSecondaryTransitionable().getIdentity();
	}

	@Override
	protected AbstractTransferringCoinOne<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newAbstractTransferringCoinOne(
			BrandsSchemeSetup<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			Bank<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			AccountHolderPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bearer,
			Group.Element<Gq> blindedIdentity, Group.Element<Gq> commitment, CoinSignature<Gq, Z> coinSignature,
			AssetType assetType, Number faceValue, FiniteField.Element<Z> s, FiniteField.Element<Z> x1,
			FiniteField.Element<Z> x2, String identity, String shop) {
		return new TransferringCoinOne((ExampleSetup) setup, (ExampleBank) bank, (ExampleAccountHolder) bearer,
				blindedIdentity, commitment, (ExampleCoinSignature) coinSignature, assetType, faceValue, s, x1, x2,
				identity, shop);
	}

}
