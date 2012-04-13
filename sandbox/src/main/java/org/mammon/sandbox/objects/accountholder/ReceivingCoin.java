package org.mammon.sandbox.objects.accountholder;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.objects.example.ExampleBank;
import org.mammon.sandbox.objects.example.ExampleCoinSignature;
import org.mammon.sandbox.objects.example.ExampleFiniteField;
import org.mammon.sandbox.objects.example.ExampleGroup;
import org.mammon.sandbox.objects.example.ExamplePaymentHashFunction;
import org.mammon.sandbox.objects.example.ExampleSetup;
import org.mammon.sandbox.objects.example.ExampleShop;
import org.mammon.sandbox.objects.example.ExampleSignatureHashFunction;
import org.mammon.sandbox.objects.example.ExampleSpentCoin;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.generic.bank.AbstractBank;
import org.mammon.scheme.brands.generic.coin.AbstractReceivingCoin;
import org.mammon.scheme.brands.generic.coin.AbstractSpentCoin;
import org.mammon.scheme.brands.shop.Shop;

public class ReceivingCoin
		extends
		AbstractReceivingCoin<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(AbstractReceivingCoin.class)
	public ReceivingCoin(@PersistAs("setup") ExampleSetup setup, @PersistAs("bank") ExampleBank bank,
			@PersistAs("blindedIdentity") Group.Element<ExampleGroup> blindedIdentity,
			@PersistAs("commitment") Group.Element<ExampleGroup> commitment,
			@PersistAs("coinSignature") CoinSignature<ExampleGroup, ExampleFiniteField> coinSignature,
			@PersistAs("assetType") AssetType assetType, @PersistAs("faceValue") Number faceValue,
			@PersistAs("shop") ExampleShop shop, @PersistAs("time") Long time) {
		super(setup, bank, blindedIdentity, commitment, coinSignature, assetType, faceValue, shop, time);
	}

	@Override
	protected AbstractSpentCoin<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newAbstractSpentCoin(
			BrandsSchemeSetup<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			AbstractBank<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			Group.Element<ExampleGroup> blindedIdentity,
			Group.Element<ExampleGroup> commitment,
			CoinSignature<ExampleGroup, ExampleFiniteField> coinSignature,
			AssetType assetType,
			Number faceValue,
			String identity,
			Shop<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bearer,
			Long time, FiniteField.Element<ExampleFiniteField> r1, FiniteField.Element<ExampleFiniteField> r2) {
		return new ExampleSpentCoin((ExampleSetup) setup, (ExampleBank) bank, blindedIdentity, commitment,
				(ExampleCoinSignature) coinSignature, assetType, faceValue, identity, (ExampleShop) bearer, time, r1,
				r2);
	}

}
