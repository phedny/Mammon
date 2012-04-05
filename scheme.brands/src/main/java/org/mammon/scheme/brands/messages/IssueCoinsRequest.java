package org.mammon.scheme.brands.messages;

import org.mammon.AssetType;
import org.mammon.math.FiniteField;
import org.mammon.math.Group;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Message;
import org.mammon.messaging.PersistAs;

public class IssueCoinsRequest<G extends Group<G>, F extends FiniteField<F>> implements Message {

	private final AssetType assetType;

	private final Number faceValue;

	private final FiniteField.Element<F> blindedChallenge;

	@FromPersistent(Message.class)
	public IssueCoinsRequest(@PersistAs("assetType") AssetType assetType, @PersistAs("faceValue") Number faceValue,
			@PersistAs("blindedChallenge") FiniteField.Element<F> blindedChallenge) {
		this.assetType = assetType;
		this.faceValue = faceValue;
		this.blindedChallenge = blindedChallenge;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public Number getFaceValue() {
		return faceValue;
	}

	public FiniteField.Element<F> getBlindedChallenge() {
		return blindedChallenge;
	}

}
