package org.mammon.sandbox.messages;

import org.mammon.messaging.Message;
import org.mammon.scheme.brands.Group;
import org.mammon.scheme.brands.Group.Element;

public class IssueCoinsResponse<G extends Group<G>> implements Message {

	private final Group.Element<G>[] response;

	public IssueCoinsResponse(Element<G>[] response) {
		this.response = response;
	}

	public Group.Element<G>[] getResponse() {
		return response;
	}

}
