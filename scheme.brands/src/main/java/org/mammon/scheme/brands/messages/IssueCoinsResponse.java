package org.mammon.scheme.brands.messages;

import org.mammon.math.FiniteField;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Message;
import org.mammon.messaging.PersistAs;

public class IssueCoinsResponse<F extends FiniteField<F>> implements Message {

	private final FiniteField.Element<F> response;

	@FromPersistent(Message.class)
	public IssueCoinsResponse(@PersistAs("response") FiniteField.Element<F> response) {
		this.response = response;
	}

	public FiniteField.Element<F> getResponse() {
		return response;
	}

}
