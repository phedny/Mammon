package org.mammon.scheme.brands.messages;

import org.mammon.math.FiniteField;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Message;
import org.mammon.messaging.PersistAs;

public class CoinTransferMessage<F extends FiniteField<F>> implements Message {

	private final FiniteField.Element<F> r1;

	private final FiniteField.Element<F> r2;

	@FromPersistent(CoinTransferMessage.class)
	public CoinTransferMessage(@PersistAs("r1") FiniteField.Element<F> r1, @PersistAs("r2") FiniteField.Element<F> r2) {
		this.r1 = r1;
		this.r2 = r2;
	}

	public FiniteField.Element<F> getR1() {
		return r1;
	}

	public FiniteField.Element<F> getR2() {
		return r2;
	}

}
