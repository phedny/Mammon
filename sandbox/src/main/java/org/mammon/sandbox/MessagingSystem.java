package org.mammon.sandbox;

public class MessagingSystem extends AbstractMessagingSystem<String> {

	public MessagingSystem(Class<?>... registerClasses) {
		super(registerClasses);
	}

	@Override
	protected Class<?>[] getMessagingClasses() {
		return new Class<?>[] { StringRedeliverableMessage.class };
	}

}
