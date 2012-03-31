package org.mammon.scheme.brands.messages;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.Message;

public class IssueCoinsRequest<G extends Group<G>, F extends FiniteField<F>>
		implements Message {

	private final Group.Element<G> firstWitness;

	private final FiniteField.Element<F> blindedChallenge;

	public IssueCoinsRequest(AssetType assetType, Number faceValue,
			Group.Element<G> firstWitness, FiniteField.Element<F> blindedChallenge) {
		this.firstWitness = firstWitness;
		this.blindedChallenge = blindedChallenge;
	}

	public Group.Element<G> getFirstWitness() {
		return firstWitness;
	}

	public FiniteField.Element<F> getBlindedChallenge() {
		return blindedChallenge;
	}

}
