package org.mammon.sandbox.generic.accountholder;

import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.Transactable;
import org.mammon.sandbox.generic.coin.AbstractWithdrawingCoinOne;
import org.mammon.sandbox.messages.ObtainCoinsMessage;
import org.mammon.sandbox.objects.accountholder.WithdrawingCoinOne;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.Group.Element;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;

public abstract class AbstractAccountHolderPrivate<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>, I> extends AbstractAccountHolderForBank<G, S, T, H, H0, I>
		implements AccountHolderPrivate<G, S, T, H, H0>, Identifiable<I>, Transactable {

	private final Element<G> privateKey;

	private final Bank<G, S, T, H, H0> bank;

	protected AbstractAccountHolderPrivate(BrandsSchemeSetup<G, S, T, H, H0> setup, Element<G> privateKey,
			Element<G> publicKey, Element<G> blindedIdentity, Bank<G, S, T, H, H0> bank) {
		super(setup, publicKey, blindedIdentity);
		this.privateKey = privateKey;
		this.bank = bank;
	}
	
	@Override
	public Element<G> getPrivateKey() {
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

	protected Bank<G, S, T, H, H0> getBank() {
		return bank;
	}

	protected abstract AbstractWithdrawingCoinOne<G, S, T, H, H0, I> newWithdrawingCoinOne(ObtainCoinsMessage<String> request);

}
