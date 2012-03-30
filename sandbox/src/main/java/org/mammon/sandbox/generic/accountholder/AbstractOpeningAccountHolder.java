package org.mammon.sandbox.generic.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transitionable;
import org.mammon.sandbox.generic.messaging.AbstractTransitionable;
import org.mammon.sandbox.messages.BlindedIdentityRequest;
import org.mammon.sandbox.messages.BlindedIdentityResponse;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;

public abstract class AbstractOpeningAccountHolder<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>, I>
		extends AbstractTransitionable<I> implements Identifiable<I>, Transitionable<I>, MessageEmitter {

	private final BrandsSchemeSetup<G, F, S, T, H, H0> setup;

	private final FiniteField.Element<F> privateKey;

	private final Group.Element<G> publicKey;

	private final Bank<G, F, S, T, H, H0> bank;

	public AbstractOpeningAccountHolder(BrandsSchemeSetup<G, F, S, T, H, H0> setup, Bank<G, F, S, T, H, H0> bank,
			FiniteField.Element<F> privateKey) {
		this.setup = setup;
		this.bank = bank;
		this.privateKey = privateKey;
		publicKey = setup.getGenerators()[1].exponentiate(privateKey);
	}

	public Object transition(BlindedIdentityResponse<G> response) {
		return newAccountHolder(response.getBlindedIdentity());
	}

	@Override
	public BlindedIdentityRequest<G, I> emitMessage() {
		return new BlindedIdentityRequest<G, I>(((Identifiable<I>) bank).getIdentity(), publicKey);
	}

	protected BrandsSchemeSetup<G, F, S, T, H, H0> getSetup() {
		return setup;
	}

	protected FiniteField.Element<F> getPrivateKey() {
		return privateKey;
	}

	protected Group.Element<G> getPublicKey() {
		return publicKey;
	}

	protected Bank<G, F, S, T, H, H0> getBank() {
		return bank;
	}

	protected abstract AccountHolderPrivate<G, F, S, T, H, H0> newAccountHolder(Group.Element<G> element);

}
