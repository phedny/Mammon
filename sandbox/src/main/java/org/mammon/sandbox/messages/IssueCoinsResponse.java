package org.mammon.sandbox.messages;

import org.mammon.brands.Group;
import org.mammon.brands.Group.Element;
import org.mammon.messaging.Message;

public class IssueCoinsResponse<G extends Group<G>> implements Message {

	private final Group.Element<G>[] response;

	public IssueCoinsResponse(Element<G>[] response) {
		this.response = response;
	}

	public Group.Element<G>[] getResponse() {
		return response;
	}
	
}
