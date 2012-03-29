package org.mammon.sandbox.generic.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.Transactable;
import org.mammon.sandbox.generic.coin.AbstractWithdrawingCoinOne;
import org.mammon.sandbox.messages.ObtainCoinsMessage;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;

public abstract class AbstractAccountHolderPrivate<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>, I>
		extends AbstractAccountHolderForBank<G, F, S, T, H, H0, I> implements
		AccountHolderPrivate<G, F, S, T, H, H0>, Identifiable<I>, Transactable {

	private final FiniteField.Element<F> privateKey;

	private final Bank<G, F, S, T, H, H0> bank;

	protected AbstractAccountHolderPrivate(
			BrandsSchemeSetup<G, F, S, T, H, H0> setup,
			FiniteField.Element<F> privateKey, Element<G> publicKey,
			Element<G> blindedIdentity, Bank<G, F, S, T, H, H0> bank) {
		super(setup, publicKey, blindedIdentity);
		this.privateKey = privateKey;
		this.bank = bank;
	}

	@Override
	public FiniteField.Element<F> getPrivateKey() {
		return privateKey;
	}

	@Override
	public Object transact(Message message) {
		if (message instanceof ObtainCoinsMessage<?>) {
			ObtainCoinsMessage<String> request = (ObtainCoinsMessage<String>) message;
			return newWithdrawingCoinOne(request);
		}
		return null;
	}

	protected Bank<G, F, S, T, H, H0> getBank() {
		return bank;
	}

	protected abstract AbstractWithdrawingCoinOne<G, F, S, T, H, H0, I> newWithdrawingCoinOne(
			ObtainCoinsMessage<String> request);

}
