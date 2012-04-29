package org.mammon.scheme.brands.generic.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Transactable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.generic.coin.AbstractWithdrawingCoinOne;
import org.mammon.scheme.brands.messages.ObtainCoinsMessage;

public abstract class AbstractAccountHolderPrivate<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>>
		extends AbstractAccountHolderForBank<G, F, T, H, H0> implements AccountHolderPrivate<G, F, T, H, H0>,
		Identifiable, Transactable {

	private final FiniteField.Element<F> privateKey;

	private final Bank<G, F, T, H, H0> bank;

	protected AbstractAccountHolderPrivate(BrandsSchemeSetup<G, F, T, H, H0> setup,
			FiniteField.Element<F> privateKey, Group.Element<G> publicKey, Group.Element<G> blindedIdentity,
			Bank<G, F, T, H, H0> bank) {
		super(setup, publicKey, blindedIdentity);
		this.privateKey = privateKey;
		this.bank = bank;
	}

	@Override
	public FiniteField.Element<F> getPrivateKey() {
		return privateKey;
	}

	public AbstractWithdrawingCoinOne<G, F, T, H, H0> transact(ObtainCoinsMessage request) {
		return newWithdrawingCoinOne(request);
	}

	public Bank<G, F, T, H, H0> getBank() {
		return bank;
	}

	protected abstract AbstractWithdrawingCoinOne<G, F, T, H, H0> newWithdrawingCoinOne(
			ObtainCoinsMessage request);

	@Override
	public String getIdentity() {
		return "accountholder-" + getBlindedIdentity();
	}

}
