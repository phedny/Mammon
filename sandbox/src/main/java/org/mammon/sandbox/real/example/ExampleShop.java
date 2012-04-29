package org.mammon.sandbox.real.example;

import java.util.Date;

import org.mammon.AssetType;
import org.mammon.Bearer;
import org.mammon.math.Gq;
import org.mammon.math.Group;
import org.mammon.math.Z;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.PersistAs;
import org.mammon.sandbox.real.accountholder.ReceivingCoin;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.CoinSignature;
import org.mammon.scheme.brands.generic.bank.AbstractBank;
import org.mammon.scheme.brands.generic.coin.AbstractReceivingCoin;
import org.mammon.scheme.brands.generic.shop.AbstractShop;

public class ExampleShop extends
		AbstractShop<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> {

	@FromPersistent(Bearer.class)
	public ExampleShop(@PersistAs("setup") ExampleSetup setup, @PersistAs("identity") String identity) {
		super(setup, identity);
	}

	@Override
	protected AbstractReceivingCoin<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> newAbstractReceivingCoin(
			BrandsSchemeSetup<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> setup,
			Bank<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> bank,
			Group.Element<Gq> blindedIdentity, Group.Element<Gq> commitment, CoinSignature<Gq, Z> coinSignature,
			AssetType assetType, Number faceValue,
			AbstractShop<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction> shop) {
		return new ReceivingCoin((ExampleSetup) setup,
				(AbstractBank<Gq, Z, String, Long, ExampleSignatureHashFunction, ExamplePaymentHashFunction>) bank,
				blindedIdentity, commitment, coinSignature, assetType, faceValue, (ExampleShop) shop, Long
						.valueOf(new Date().getTime()));
	}

}
