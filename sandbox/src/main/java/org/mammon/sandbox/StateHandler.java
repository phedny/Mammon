package org.mammon.sandbox;

public interface StateHandler<C, I> {

	void enteredState(C object, I enteredBy);
	
	void leftState(C object);
	
}
