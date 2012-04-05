package org.mammon.scheme.brands.generic.bank;

import java.util.HashMap;
import java.util.Map;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transactable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.messages.BankWitnessesRequest;
import org.mammon.scheme.brands.messages.BlindedIdentityResponse;
import org.mammon.util.messaging.AbstractTransactable;

public abstract class AbstractBlindedIdentity<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		extends AbstractTransactable implements Identifiable, Transactable, MessageEmitter {

	private final BrandsSchemeSetup<G, F, I, T, H, H0> setup;

	private final AbstractBankPrivate<G, F, I, T, H, H0> bank;

	private final Group.Element<G> payerIdentity;

	private Map<Group.Element<G>, FiniteField.Element<F>> issuedWitnesses = new HashMap<Group.Element<G>, FiniteField.Element<F>>();

	public AbstractBlindedIdentity(BrandsSchemeSetup<G, F, I, T, H, H0> setup,
			AbstractBankPrivate<G, F, I, T, H, H0> bank, Group.Element<G> payerIdentity) {
		this.setup = setup;
		this.bank = bank;
		this.payerIdentity = payerIdentity;
	}

	public BrandsSchemeSetup<G, F, I, T, H, H0> getSetup() {
		return setup;
	}

	public AbstractBankPrivate<G, F, I, T, H, H0> getBank() {
		return bank;
	}

	public Group.Element<G> getPayerIdentity() {
		return payerIdentity;
	}

	@Override
	public Message emitMessage() {
		return new BlindedIdentityResponse<G>(payerIdentity);
	}

	public AbstractIssuedWitnesses<G,F,I,T,H,H0> transact(BankWitnessesRequest<G> request) {
		final Group.Element<G> identity = request.getIdentity();
		for (int i = 0; i < request.getCount(); i++) {
			final FiniteField.Element<F> w = getSetup().getFiniteField().getRandomElement();
			return newAbstractIssuedWitnesses(setup, bank, payerIdentity, w);
		}
		return null;
	}

	protected abstract AbstractIssuedWitnesses<G, F, I, T, H, H0> newAbstractIssuedWitnesses(
			BrandsSchemeSetup<G, F, I, T, H, H0> setup, AbstractBankPrivate<G, F, I, T, H, H0> bank,
			Group.Element<G> payerIdentity, FiniteField.Element<F> w);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bank == null) ? 0 : bank.hashCode());
		result = prime * result + ((issuedWitnesses == null) ? 0 : issuedWitnesses.hashCode());
		result = prime * result + ((payerIdentity == null) ? 0 : payerIdentity.hashCode());
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
		AbstractBlindedIdentity other = (AbstractBlindedIdentity) obj;
		if (bank == null) {
			if (other.bank != null)
				return false;
		} else if (!bank.equals(other.bank))
			return false;
		if (issuedWitnesses == null) {
			if (other.issuedWitnesses != null)
				return false;
		} else if (!issuedWitnesses.equals(other.issuedWitnesses))
			return false;
		if (payerIdentity == null) {
			if (other.payerIdentity != null)
				return false;
		} else if (!payerIdentity.equals(other.payerIdentity))
			return false;
		if (setup == null) {
			if (other.setup != null)
				return false;
		} else if (!setup.equals(other.setup))
			return false;
		return true;
	}

}
