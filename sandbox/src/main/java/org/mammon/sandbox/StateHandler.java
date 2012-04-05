package org.mammon.sandbox;

public interface StateHandler<C> {

	void enteredState(C object, String enteredBy);
	
	void leftState(C object);
	
}
