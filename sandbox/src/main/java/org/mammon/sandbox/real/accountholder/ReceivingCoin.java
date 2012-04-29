package org.mammon.sandbox.real.accountholder;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.real.example.ExampleBank;
import org.mammon.sandbox.real.example.ExampleCoinSignature;
import org.mammon.sandbox.real.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.real.example.ExampleSetup;
import org.mammon.sandbox.real.example.ExampleShop;
import org.mammon.sandbox.real.example.ExampleSignatureHashFunction;
import org.mammon.sandbox.real.example.ExampleSpentCoin;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.generic.bank.AbstractBank;
import org.mammon.scheme.brands.generic.coin.AbstractReceivingCoin;
import org.mammon.scheme.brands.generic.coin.AbstractSpentCoin;
import org.mammon.scheme.brands.shop.Shop;

public class ReceivingCoin extends
		AbstractReceivingCoin<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(AbstractReceivingCoin.class)
	public ReceivingCoin(
			@PersistAs("setup") ExampleSetup setup,
			@PersistAs("bank") AbstractBank<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			@PersistAs("blindedIdentity") Group.Element<Gq> blindedIdentity,
			@PersistAs("commitment") Group.Element<Gq> commitment,
			@PersistAs("coinSignature") CoinSignature<Gq, Z> coinSignature,
			@PersistAs("assetType") AssetType assetType, @PersistAs("faceValue") Number faceValue,
			@PersistAs("shop") ExampleShop shop, @PersistAs("time") Long time) {
		super(setup, bank, blindedIdentity, commitment, coinSignature, assetType, faceValue, shop, time);
	}

	@Override
	protected AbstractSpentCoin<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newAbstractSpentCoin(
			BrandsSchemeSetup<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			AbstractBank<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			Group.Element<Gq> blindedIdentity, Group.Element<Gq> commitment, CoinSignature<Gq, Z> coinSignature,
			AssetType assetType, Number faceValue, String identity,
			Shop<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bearer, Long time,
			FiniteField.Element<Z> r1, FiniteField.Element<Z> r2) {
		return new ExampleSpentCoin((ExampleSetup) setup,
				(AbstractBank<Gq, Z, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) bank,
				blindedIdentity, commitment, (ExampleCoinSignature) coinSignature, assetType, faceValue, identity,
				(ExampleShop) bearer, time, r1, r2);
	}

	@Override
	public Long getTime() {
		return super.getTime();
	}

}
