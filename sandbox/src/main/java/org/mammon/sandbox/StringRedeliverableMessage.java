package org.mammon.sandbox;

import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.PersistAs;
import org.mammon.messaging.Transitionable;

public class StringRedeliverableMessage implements Identifiable, MessageEmitter, Transitionable {

	private final String identity;

	private final Message incomingMessage;

	private final Message outgoingMessage;

	@FromPersistent(StringRedeliverableMessage.class)
	public StringRedeliverableMessage(@PersistAs("identity") String identity,
			@PersistAs("incomingMessage") Message incomingMessage, @PersistAs("outgoingMessage") Message outgoingMessage) {
		this.identity = identity;
		this.incomingMessage = incomingMessage;
		this.outgoingMessage = outgoingMessage;
	}

	@Override
	public Message emitMessage() {
		return outgoingMessage;
	}

	@Override
	public String getIdentity() {
		return identity;
	}

	public Message getIncomingMessage() {
		return incomingMessage;
	}

	public Message getOutgoingMessage() {
		return outgoingMessage;
	}

	@Override
	public Object transition(Message message) {
		if (incomingMessage.equals(message)) {
			return this;
		}
		return null;
	}

}
