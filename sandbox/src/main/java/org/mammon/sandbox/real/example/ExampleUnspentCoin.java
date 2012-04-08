package org.mammon.sandbox.real.example;

import java.util.UUID;

import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.coin.UnspentCoin;
import org.mammon.scheme.brands.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.scheme.brands.generic.bank.AbstractBankPrivate;
import org.mammon.scheme.brands.generic.coin.AbstractUnspentCoin;

public class ExampleUnspentCoin
		extends
		AbstractUnspentCoin<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(UnspentCoin.class)
	public ExampleUnspentCoin(@PersistAs("setup") ExampleSetup setup, @PersistAs("bearer") AbstractAccountHolderPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bearer,
			@PersistAs("issuer") AbstractBankPrivate<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank, @PersistAs("dualIdentity") String dualIdentity,
			@PersistAs("blindingFactor") FiniteField.Element<Z> blindingFactor,
			@PersistAs("payerWitness1") FiniteField.Element<Z> x1,
			@PersistAs("payerWitness2") FiniteField.Element<Z> x2,
			@PersistAs("blindedIdentity") Group.Element<Gq> blindedIdentity,
			@PersistAs("commitment") Group.Element<Gq> commitment,
			@PersistAs("r") FiniteField.Element<Z> r,
			@PersistAs("coinSignature") CoinSignature<Gq, Z> coinSignature) {
		super(setup, bearer, bank, UUID.randomUUID().toString(), dualIdentity, blindingFactor, x1, x2, blindedIdentity,
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

}
