package org.mammon.sandbox.generic;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.math.Group.Element;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
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

public abstract class AbstractBankPrivate<G extends Group<G>, F extends FiniteField<F>, S, T, H extends SignatureHashFunction<G, F>, H0 extends PaymentHashFunction<G, F, S, T>, I>
		extends AbstractBank<G, F, S, T, H, H0, I> implements
		BankPrivate<G, F, S, T, H, H0>, Identifiable<I>, Transactable {

	private final FiniteField.Element<F> privateKey;
	private Set<Element<G>> knownIdentities = new HashSet<Element<G>>();
	private Map<Group.Element<G>, FiniteField.Element<F>> issuedWitnesses = new HashMap<Group.Element<G>, FiniteField.Element<F>>();

	protected AbstractBankPrivate(BrandsSchemeSetup<G, F, S, T, H, H0> setup,
			FiniteField.Element<F> privateKey) {
		super(setup, setup.getGenerators()[0].exponentiate(privateKey));
		this.privateKey = privateKey;
	}

	@Override
	public FiniteField.Element<F> getPrivateKey() {
		return privateKey;
	}

	@Override
	public Message transact(Message message) {
		if (message instanceof BlindedIdentityRequest<?, ?>) {
			BlindedIdentityRequest<G, ?> request = (BlindedIdentityRequest<G, ?>) message;
			knownIdentities.add(request.getIdentity());
			return new BlindedIdentityResponse<G>(request.getIdentity()
					.multiply(getSetup().getGenerators()[2])
					.exponentiate(privateKey));
		} else if (message instanceof BankWitnessesRequest<?, ?>) {
			BankWitnessesRequest<G, ?> request = (BankWitnessesRequest<G, ?>) message;
			final Element<G> identity = request.getIdentity();
			if (!knownIdentities.contains(identity)) {
				return new BankWitnessesResponse<G>(null);
			}
			Element<G>[] witnesses = (Element<G>[]) Array.newInstance(
					Element.class, 2 * request.getCount());
			for (int i = 0; i < request.getCount(); i++) {
				final FiniteField.Element<F> w = getSetup().getFiniteField()
						.getRandomElement();
				Element<G> a = getSetup().getGenerators()[0].exponentiate(w);
				Element<G> b = identity.multiply(getSetup().getGenerators()[2])
						.exponentiate(w);

				issuedWitnesses.put(a, w);
				witnesses[2 * i] = a;
				witnesses[2 * i + 1] = b;
			}
			return new BankWitnessesResponse<G>(witnesses);
		} else if (message instanceof IssueCoinsRequest<?, ?>) {
			IssueCoinsRequest<G, F> request = (IssueCoinsRequest<G, F>) message;
			if (request.getFirstWitness().length != request
					.getBlindedChallenge().length) {
				return new IssueCoinsResponse<F>(null);
			}
			FiniteField.Element<F>[] response = (FiniteField.Element<F>[]) Array
					.newInstance(FiniteField.Element.class,
							request.getBlindedChallenge().length);
			for (int i = 0; i < request.getBlindedChallenge().length; i++) {
				Element<G> witness = request.getFirstWitness()[i];
				FiniteField.Element<F> secretWitness = issuedWitnesses
						.get(witness);
				if (secretWitness != null) {
					issuedWitnesses.remove(witness);
					response[i] = request.getBlindedChallenge()[i].multiply(
							privateKey).add(secretWitness);
				}
			}
			return new IssueCoinsResponse<F>(response);
		}
		return null;
	}

}