package org.mammon.sandbox.generic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Transactable;
import org.mammon.sandbox.messages.BankWitnessesRequest;
import org.mammon.sandbox.messages.BankWitnessesResponse;
import org.mammon.sandbox.messages.BlindedIdentityRequest;
import org.mammon.sandbox.messages.BlindedIdentityResponse;
import org.mammon.sandbox.messages.IssueCoinsRequest;
import org.mammon.sandbox.messages.IssueCoinsResponse;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.bank.BankPrivate;

public abstract class AbstractBankPrivate<G extends Group<G>, F extends FiniteField<F>, I, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, I, T>>
		extends AbstractBank<G, F, I, T, H, H0> implements BankPrivate<G, F, I, T, H, H0>, Identifiable<I>,
		Transactable<I> {

	private final FiniteField.Element<F> privateKey;
	private Set<Group.Element<G>> knownIdentities = new HashSet<Group.Element<G>>();
	private Map<Group.Element<G>, FiniteField.Element<F>> issuedWitnesses = new HashMap<Group.Element<G>, FiniteField.Element<F>>();

	protected AbstractBankPrivate(BrandsSchemeSetup<G, F, I, T, H, H0> setup, FiniteField.Element<F> privateKey) {
		super(setup, setup.getGenerators()[0].exponentiate(privateKey));
		this.privateKey = privateKey;
	}

	@Override
	public FiniteField.Element<F> getPrivateKey() {
		return privateKey;
	}

	public BlindedIdentityResponse<G> transact(BlindedIdentityRequest<G, ?> request) {
		knownIdentities.add(request.getIdentity());
		return new BlindedIdentityResponse<G>(request.getIdentity().multiply(getSetup().getGenerators()[2])
				.exponentiate(privateKey));
	}

	public BankWitnessesResponse<G> transact(BankWitnessesRequest<G, ?> request) {
		final Group.Element<G> identity = request.getIdentity();
		if (!knownIdentities.contains(identity)) {
			return new BankWitnessesResponse<G>(null, null);
		}
		for (int i = 0; i < request.getCount(); i++) {
			final FiniteField.Element<F> w = getSetup().getFiniteField().getRandomElement();
			Group.Element<G> a = getSetup().getGenerators()[0].exponentiate(w);
			Group.Element<G> b = identity.multiply(getSetup().getGenerators()[2]).exponentiate(w);
			issuedWitnesses.put(a, w);
			return new BankWitnessesResponse<G>(a, b);
		}
		return new BankWitnessesResponse<G>(null, null);
	}

	public IssueCoinsResponse<F> transact(IssueCoinsRequest<G, F> request) {
		Group.Element<G> witness = request.getFirstWitness();
		FiniteField.Element<F> secretWitness = issuedWitnesses.get(witness);
		if (secretWitness != null) {
			issuedWitnesses.remove(witness);
			return new IssueCoinsResponse<F>(request.getBlindedChallenge().multiply(privateKey).add(secretWitness));
		}
		return new IssueCoinsResponse<F>(null);
	}

}