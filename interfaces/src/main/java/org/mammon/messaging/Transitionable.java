package org.mammon.messaging;

public interface Transitionable {

	Object transition(Message message);
	
}
