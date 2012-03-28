package org.mammon.sandbox.objects.accountholder;

import org.mammon.brands.Bank;
import org.mammon.brands.BrandsSchemeSetup;
import org.mammon.brands.Group;
import org.mammon.brands.PaymentHashFunction;
import org.mammon.brands.SignatureHashFunction;
import org.mammon.brands.Group.Element;
import org.mammon.messaging.DirectedMessage;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transitionable;
import org.mammon.sandbox.messages.BlindedIdentityRequest;
import org.mammon.sandbox.messages.BlindedIdentityResponse;
import org.mammon.sandbox.objects.example.ExampleAccountHolder;

public class OpeningAccountHolder<G extends Group<G>, S, T, H extends SignatureHashFunction<G>, H0 extends PaymentHashFunction<G, S, T>>
		implements Identifiable<String>, Transitionable, MessageEmitter {

	private final BrandsSchemeSetup<G, S, T, H, H0> setup;

	private final Element<G> privateKey;

	private final Element<G> publicKey;

	private final Bank bank;

	public OpeningAccountHolder(BrandsSchemeSetup<G, S, T, H, H0> setup, Bank bank) {
		this.setup = setup;
		this.bank = bank;
		privateKey = setup.getGroup().getRandomElement(null);
		publicKey = setup.getGenerators()[1].exponentiate(privateKey);
	}

	@Override
	public String getIdentity() {
		return "opening-" + publicKey.toString();
	}

	@Override
	public Object transition(Message message) {
		if (message instanceof BlindedIdentityResponse<?>) {
			BlindedIdentityResponse<G> response = (BlindedIdentityResponse<G>) message;
			return new ExampleAccountHolder<G, S, T, H, H0>(setup, privateKey, publicKey,
					response.getBlindedIdentity(), bank);
		}
		return null;
	}

	@Override
	public DirectedMessage emitMessage() {
		return new BlindedIdentityRequest<G, String>(((Identifiable<String>) bank).getIdentity(), publicKey);
	}

}
