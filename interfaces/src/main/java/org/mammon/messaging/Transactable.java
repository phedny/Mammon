package org.mammon.messaging;

public interface Transactable<I> extends Identifiable<I> {

	Object transact(Message message);
	
}
