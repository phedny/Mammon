package org.mammon.sandbox.objects.example;

import java.util.Date;

import org.mammon.AssetType;
import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.objects.accountholder.ReceivingCoin;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.generic.coin.AbstractReceivingCoin;
import org.mammon.scheme.brands.generic.shop.AbstractShop;
import org.mammon.scheme.brands.shop.Shop;

public class ExampleShop
		extends
		AbstractShop<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(Shop.class)
	public ExampleShop(@PersistAs("setup") ExampleSetup setup, @PersistAs("identity") String identity) {
		super(setup, identity);
	}

	@Override
	protected AbstractReceivingCoin<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newAbstractReceivingCoin(
			BrandsSchemeSetup<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			Bank<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			Group.Element<ExampleGroup> blindedIdentity,
			Group.Element<ExampleGroup> commitment,
			CoinSignature<ExampleGroup, ExampleFiniteField> coinSignature,
			AssetType assetType,
			Number faceValue,
			AbstractShop<ExampleGroup, ExampleFiniteField, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> shop) {
		return new ReceivingCoin((ExampleSetup) setup, (ExampleBank) bank, blindedIdentity, commitment, coinSignature,
				assetType, faceValue, (ExampleShop) shop, Long.valueOf(new Date().getTime()));
	}
}
