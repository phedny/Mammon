package org.mammon.sandbox.messages;

import org.mammon.math.FiniteField;
import org.mammon.messaging.Message;

public class IssueCoinsResponse<F extends FiniteField<F>> implements Message {

	private final FiniteField.Element<F>[] response;

	public IssueCoinsResponse(FiniteField.Element<F>[] response) {
		this.response = response;
	}

	public FiniteField.Element<F>[] getResponse() {
		return response;
	}

}
