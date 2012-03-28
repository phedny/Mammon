package org.mammon.sandbox.generic.accountholder;

import org.mammon.messaging.DirectedMessage;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transitionable;
import org.mammon.sandbox.messages.BlindedIdentityRequest;
import org.mammon.sandbox.messages.BlindedIdentityResponse;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.Group.Element;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;

public abstract class AbstractOpeningAccountHolder<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>, I>
		implements Identifiable<I>, Transitionable, MessageEmitter {

	private final BrandsSchemeSetup<G, S, T, H, H0> setup;

	private final Element<G> privateKey;

	private final Element<G> publicKey;

	private final Bank<G, S, T, H, H0> bank;

	public AbstractOpeningAccountHolder(BrandsSchemeSetup<G, S, T, H, H0> setup, Bank<G, S, T, H, H0> bank,
			Element<G> privateKey) {
		this.setup = setup;
		this.bank = bank;
		this.privateKey = privateKey;
		publicKey = setup.getGenerators()[1].exponentiate(privateKey);
	}

	@Override
	public Object transition(Message message) {
		if (message instanceof BlindedIdentityResponse<?>) {
			BlindedIdentityResponse<G> response = (BlindedIdentityResponse<G>) message;
			return newAccountHolder(response.getBlindedIdentity());
		}
		return null;
	}

	@Override
	public DirectedMessage emitMessage() {
		return new BlindedIdentityRequest<G, String>(((Identifiable<String>) bank).getIdentity(), publicKey);
	}

	protected BrandsSchemeSetup<G, S, T, H, H0> getSetup() {
		return setup;
	}

	protected Element<G> getPrivateKey() {
		return privateKey;
	}

	protected Element<G> getPublicKey() {
		return publicKey;
	}

	protected Bank<G, S, T, H, H0> getBank() {
		return bank;
	}

	protected abstract AccountHolderPrivate<G, S, T, H, H0> newAccountHolder(Element<G> element);

}
