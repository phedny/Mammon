package org.mammon.sandbox.objects.example;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.Transactable;
import org.mammon.sandbox.HashCodeUtil;
import org.mammon.sandbox.messages.BankWitnessesRequest;
import org.mammon.sandbox.messages.BankWitnessesResponse;
import org.mammon.sandbox.messages.BlindedIdentityRequest;
import org.mammon.sandbox.messages.BlindedIdentityResponse;
import org.mammon.sandbox.messages.IssueCoinsRequest;
import org.mammon.sandbox.messages.IssueCoinsResponse;
import org.mammon.scheme.brands.BrandsSchemeSetup;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.PaymentHashFunction;
import org.mammon.scheme.brands.SignatureHashFunction;
import org.mammon.scheme.brands.Group.Element;
import org.mammon.scheme.brands.bank.BankPrivate;

public class ExampleBank<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		implements BankPrivate<G, S, T, H, H0>, Identifiable<String>, Transactable {

	private final BrandsSchemeSetup<G, S, T, H, H0> setup;

	private final Element<G> privateKey;

	private final Element<G> publicKey;

	private Set<Element<G>> knownIdentities = new HashSet<Element<G>>();

	private Map<Element<G>, Element<G>> issuedWitnesses = new HashMap<Element<G>, Element<G>>();

	public ExampleBank(BrandsSchemeSetup<G, S, T, H, H0> setup) {
		this.setup = setup;
		G group = setup.getGroup();
		privateKey = group.getRandomElement(null);
		publicKey = setup.getGenerators()[0].exponentiate(privateKey);
	}

	@Override
	public Element<G> getPrivateKey() {
		return privateKey;
	}

	@Override
	public Element<G> getPublicKey() {
		return publicKey;
	}

	@Override
	public BrandsSchemeSetup<G, S, T, H, H0> getSetup() {
		return setup;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ExampleBank<?, ?, ?, ?, ?>)) {
			return false;
		}
		ExampleBank<?, ?, ?, ?, ?> other = (ExampleBank<?, ?, ?, ?, ?>) obj;
		return setup.equals(other.setup) && publicKey.equals(other.publicKey);
	}

	@Override
	public int hashCode() {
		int hashCode = HashCodeUtil.SEED;
		hashCode = HashCodeUtil.hash(hashCode, setup);
		hashCode = HashCodeUtil.hash(hashCode, publicKey);
		return publicKey.hashCode();
	}

	@Override
	public String toString() {
		return "ExampleBank(" + setup.hashCode() + "," + publicKey.toString() + ")";
	}

	@Override
	public Message transact(Message message) {
		if (message instanceof BlindedIdentityRequest<?, ?>) {
			BlindedIdentityRequest<G, ?> request = (BlindedIdentityRequest<G, ?>) message;
			knownIdentities.add(request.getIdentity());
			return new BlindedIdentityResponse<G>(request.getIdentity().multiply(setup.getGenerators()[2])
					.exponentiate(privateKey));
		} else if (message instanceof BankWitnessesRequest<?, ?>) {
			BankWitnessesRequest<G, ?> request = (BankWitnessesRequest<G, ?>) message;
			final Element<G> identity = request.getIdentity();
			if (!knownIdentities.contains(identity)) {
				return new BankWitnessesResponse<G>(null);
			}
			Element<G>[] witnesses = (Element<G>[]) Array.newInstance(Element.class, 2 * request.getCount());
			for (int i = 0; i < request.getCount(); i++) {
				final Element<G> w = setup.getGroup().getRandomElement(null);
				Element<G> a = setup.getGenerators()[0].exponentiate(w);
				Element<G> b = identity.multiply(setup.getGenerators()[2]).exponentiate(w);

				issuedWitnesses.put(a, w);
				witnesses[2 * i] = a;
				witnesses[2 * i + 1] = b;
			}
			return new BankWitnessesResponse<G>(witnesses);
		} else if (message instanceof IssueCoinsRequest<?>) {
			IssueCoinsRequest<G> request = (IssueCoinsRequest<G>) message;
			if (request.getFirstWitness().length != request.getBlindedChallenge().length) {
				return new IssueCoinsResponse<G>(null);
			}
			Element<G>[] response = (Element<G>[]) Array.newInstance(Element.class,
					request.getBlindedChallenge().length);
			for (int i = 0; i < request.getBlindedChallenge().length; i++) {
				Element<G> witness = request.getFirstWitness()[i];
				Element<G> secretWitness = issuedWitnesses.get(witness);
				if (secretWitness != null) {
					issuedWitnesses.remove(witness);
					response[i] = request.getBlindedChallenge()[i].multiply(privateKey).add(secretWitness);
				}
			}
			return new IssueCoinsResponse<G>(response);
		}
		return null;
	}

	@Override
	public String getIdentity() {
		return "bank-" + publicKey.toString();
	}
}
