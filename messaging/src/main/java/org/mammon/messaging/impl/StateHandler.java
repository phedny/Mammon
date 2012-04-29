package org.mammon.messaging.impl;

public interface StateHandler<C> {

	void enteredState(C object, String enteredBy);
	
	void leftState(C object);
	
}
