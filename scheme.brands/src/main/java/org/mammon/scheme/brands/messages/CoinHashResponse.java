package org.mammon.scheme.brands.messages;

import org.mammon.math.FiniteField;
import org.mammon.math.FiniteField.Element;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Message;
import org.mammon.messaging.PersistAs;

public class CoinHashResponse<F extends FiniteField<F>> implements Message {

	private final FiniteField.Element<F> hash;

	@FromPersistent(CoinHashResponse.class)
	public CoinHashResponse(@PersistAs("hash") Element<F> hash) {
		this.hash = hash;
	}

	public FiniteField.Element<F> getHash() {
		return hash;
	}

}
