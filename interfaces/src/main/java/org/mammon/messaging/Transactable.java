package org.mammon.messaging;

public interface Transactable {

	Object transact(Message message);
	
}
