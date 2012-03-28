package org.mammon.sandbox.generic.coin;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.mammon.AssetType;
import org.mammon.sandbox.HashCodeUtil;
import org.mammon.sandbox.generic.accountholder.AbstractAccountHolderPrivate;
import org.mammon.sandbox.objects.example.ExampleShop;
import org.mammon.sandbox.objects.example.ExampleGroup.ExampleElement;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.Group.Element;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.coin.SpentCoin;
import org.mammon.scheme.brands.coin.UnspentCoin;
import org.mammon.scheme.brands.shop.Shop;

public class AbstractSpentCoin<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>, I>
		implements SpentCoin<G, S, T, H, H0> {

	private final BrandsSchemeSetup<G, S, T, H, H0> setup;

	private final Bank<G, S, T, H, H0> bank;

	private final Shop<G, S, T, H, H0> bearer;

	private final T time;

	private final Element<G> blindedIdentity;

	private final Element<G> commitment;

	private final Element<G>[] coinSignature;

	private final Element<G>[] spendingCommitments;

	private final AssetType assetType;

	private final Number faceValue;

	@SuppressWarnings("unchecked")
	public AbstractSpentCoin(UnspentCoin<G, S, T, H, H0> basedOnCoin, Bank<G, S, T, H, H0> bank,
			ExampleShop<G, S, T, H, H0> bearer, T time) {
		setup = basedOnCoin.getSetup();
		this.bank = bank;
		this.bearer = bearer;
		this.time = time;

		blindedIdentity = basedOnCoin.getBlindedIdentity();
		commitment = basedOnCoin.getCommitment();
		coinSignature = basedOnCoin.getCoinSignature();
		assetType = basedOnCoin.getAssetType();
		faceValue = basedOnCoin.getFaceValue();

		// Generated by shop
		Element<G> d = setup.getPaymentHash().hash(blindedIdentity, commitment, bearer.getIdentity(), time);

		// Generated by account holder
		spendingCommitments = (Element<G>[]) Array.newInstance(Element.class, 2);
		spendingCommitments[0] = d.multiply(
				((AbstractAccountHolderPrivate<G, S, T, H, H0, I>) basedOnCoin.getBearer()).getPrivateKey()).multiply(
				basedOnCoin.getBlindingFactor()).add(basedOnCoin.getPayerWitness()[0]);
		spendingCommitments[1] = d.multiply(basedOnCoin.getBlindingFactor()).add(basedOnCoin.getPayerWitness()[1]);

		// Tested by shop
		ExampleElement left = (ExampleElement) setup.getGenerators()[1].exponentiate(spendingCommitments[0]).multiply(
				setup.getGenerators()[2].exponentiate(spendingCommitments[1]));
		ExampleElement right = (ExampleElement) blindedIdentity.exponentiate(d).multiply(commitment);
		System.out.println(left.simplify() + " <= from: " + left);
		System.out.println(right.simplify() + " <= from: " + right);
		System.out.println(left.simplify().equals(right.simplify()));
	}

	@Override
	public Shop<G, S, T, H, H0> getBearer() {
		return bearer;
	}

	@Override
	public Element<G>[] getSpendingCommitments() {
		return spendingCommitments;
	}

	@Override
	public T getTime() {
		return time;
	}

	@Override
	public Element<G> getBlindedIdentity() {
		return blindedIdentity;
	}

	@Override
	public Element<G>[] getCoinSignature() {
		return coinSignature;
	}

	@Override
	public Element<G> getCommitment() {
		return commitment;
	}

	@Override
	public Bank<G, S, T, H, H0> getIssuer() {
		return bank;
	}

	@Override
	public BrandsSchemeSetup<G, S, T, H, H0> getSetup() {
		return setup;
	}

	@Override
	public AssetType getAssetType() {
		return assetType;
	}

	@Override
	public Number getFaceValue() {
		return faceValue;
	}

	@Override
	public boolean isSellable() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractSpentCoin<?, ?, ?, ?, ?, ?>)) {
			return false;
		}
		AbstractSpentCoin<?, ?, ?, ?, ?, ?> other = (AbstractSpentCoin<?, ?, ?, ?, ?, ?>) obj;
		return setup.equals(other.setup) && bank.equals(other.bank) && bearer.equals(other.bearer)
				&& blindedIdentity.equals(other.blindedIdentity) && commitment.equals(other.commitment)
				&& Arrays.deepEquals(coinSignature, other.coinSignature);
	}

	@Override
	public int hashCode() {
		int hashCode = HashCodeUtil.SEED;
		hashCode = HashCodeUtil.hash(hashCode, setup);
		hashCode = HashCodeUtil.hash(hashCode, bank);
		hashCode = HashCodeUtil.hash(hashCode, bearer);
		hashCode = HashCodeUtil.hash(hashCode, blindedIdentity);
		hashCode = HashCodeUtil.hash(hashCode, commitment);
		hashCode = HashCodeUtil.hash(hashCode, coinSignature);
		return hashCode;
	}

}
