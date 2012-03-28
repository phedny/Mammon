package org.mammon.sandbox.messages;

import org.mammon.AssetType;
import org.mammon.messaging.Message;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.Group.Element;

public class IssueCoinsRequest<G extends Group<G>> implements Message {

	private final Group.Element<G>[] firstWitness;

	private final Group.Element<G>[] blindedChallenge;

	public IssueCoinsRequest(AssetType assetType, Number faceValue, Element<G>[] firstWitness,
			Element<G>[] blindedChallenge) {
		this.firstWitness = firstWitness;
		this.blindedChallenge = blindedChallenge;
	}

	public Group.Element<G>[] getFirstWitness() {
		return firstWitness;
	}

	public Group.Element<G>[] getBlindedChallenge() {
		return blindedChallenge;
	}

}
