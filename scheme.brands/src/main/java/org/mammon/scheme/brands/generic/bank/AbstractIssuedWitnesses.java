package org.mammon.scheme.brands.generic.bank;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transitionable;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.messages.BankWitnessesResponse;
import org.mammon.scheme.brands.messages.IssueCoinsRequest;
import org.mammon.scheme.brands.messages.IssueCoinsResponse;
import org.mammon.util.messaging.AbstractTransitionable;

public abstract class AbstractIssuedWitnesses<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		extends AbstractTransitionable implements Identifiable, Transitionable, MessageEmitter {

	private final BrandsSchemeSetup<G, F, I, T, H, H0> setup;

	private final AbstractBankPrivate<G, F, I, T, H, H0> bank;

	private final Group.Element<G> payerIdentity;

	private final FiniteField.Element<F> w;

	public AbstractIssuedWitnesses(BrandsSchemeSetup<G, F, I, T, H, H0> setup,
			AbstractBankPrivate<G, F, I, T, H, H0> bank, Group.Element<G> payerIdentity, FiniteField.Element<F> w) {
		this.setup = setup;
		this.bank = bank;
		this.payerIdentity = payerIdentity;
		this.w = w;
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

	public FiniteField.Element<F> getW() {
		return w;
	}

	public Group.Element<G> getA() {
		return getSetup().getGenerator(0).exponentiate(w);
	}

	public Group.Element<G> getB() {
		return getPayerIdentity().multiply(getSetup().getGenerator(2)).exponentiate(w);
	}

	@Override
	public Message emitMessage() {
		return new BankWitnessesResponse<G>(getA(), getB());
	}

	public IssueCoinsResponse<F> transition(IssueCoinsRequest<G, F> request) {
		return new IssueCoinsResponse<F>(request.getBlindedChallenge().multiply(getBank().getPrivateKey()).add(w));
	}

	@Override
	public String getIdentity() {
		return "issuedwitnesses-" + getBank().getIdentity() + "-" + getPayerIdentity() + "-" + getA();
	}

}
