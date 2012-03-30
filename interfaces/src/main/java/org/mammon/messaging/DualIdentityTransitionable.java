package org.mammon.messaging;

public interface DualIdentityTransitionable<I> extends Transitionable<I> {

	Transitionable<I> getSecondaryTransitionable();
	
}
