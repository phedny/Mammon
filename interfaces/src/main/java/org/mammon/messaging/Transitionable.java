package org.mammon.messaging;

public interface Transitionable<I> extends Identifiable<I> {

	Object transition(Message message);
	
}
