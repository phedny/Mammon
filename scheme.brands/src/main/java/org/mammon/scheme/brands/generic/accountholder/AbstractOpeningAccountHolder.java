package org.mammon.scheme.brands.generic.accountholder;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transitionable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.accountholder.AccountHolderPrivate;
import org.mammon.scheme.brands.bank.Bank;
import org.mammon.scheme.brands.messages.BlindedIdentityRequest;
import org.mammon.scheme.brands.messages.BlindedIdentityResponse;
import org.mammon.util.messaging.AbstractTransitionable;

public abstract class AbstractOpeningAccountHolder<G extends Group<G>, F extends FiniteField<F>, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, T>>
		extends AbstractTransitionable implements Identifiable, Transitionable, MessageEmitter {

	private final BrandsSchemeSetup<G, F, T, H, H0> setup;

	private final FiniteField.Element<F> privateKey;

	private final Group.Element<G> publicKey;

	private final Bank<G, F, T, H, H0> bank;

	public AbstractOpeningAccountHolder(BrandsSchemeSetup<G, F, T, H, H0> setup, Bank<G, F, T, H, H0> bank,
			FiniteField.Element<F> privateKey) {
		this.setup = setup;
		this.bank = bank;
		this.privateKey = privateKey;
		publicKey = setup.getGenerator(1).exponentiate(privateKey);
	}

	public Object transition(BlindedIdentityResponse<G> response) {
		return newAccountHolder(response.getBlindedIdentity());
	}

	@Override
	public BlindedIdentityRequest<G> emitMessage() {
		return new BlindedIdentityRequest<G>(((Identifiable) bank).getIdentity(), publicKey);
	}

	public BrandsSchemeSetup<G, F, T, H, H0> getSetup() {
		return setup;
	}

	public FiniteField.Element<F> getPrivateKey() {
		return privateKey;
	}

	public Group.Element<G> getPublicKey() {
		return publicKey;
	}

	public Bank<G, F, T, H, H0> getBank() {
		return bank;
	}

	protected abstract AccountHolderPrivate<G, F, T, H, H0> newAccountHolder(Group.Element<G> element);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bank == null) ? 0 : bank.hashCode());
		result = prime * result + ((privateKey == null) ? 0 : privateKey.hashCode());
		result = prime * result + ((publicKey == null) ? 0 : publicKey.hashCode());
		result = prime * result + ((setup == null) ? 0 : setup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractOpeningAccountHolder other = (AbstractOpeningAccountHolder) obj;
		if (bank == null) {
			if (other.bank != null)
				return false;
		} else if (!bank.equals(other.bank))
			return false;
		if (privateKey == null) {
			if (other.privateKey != null)
				return false;
		} else if (!privateKey.equals(other.privateKey))
			return false;
		if (publicKey == null) {
			if (other.publicKey != null)
				return false;
		} else if (!publicKey.equals(other.publicKey))
			return false;
		if (setup == null) {
			if (other.setup != null)
				return false;
		} else if (!setup.equals(other.setup))
			return false;
		return true;
	}

	@Override
	public String getIdentity() {
		return "opening-" + getPublicKey().toString();
	}

}
