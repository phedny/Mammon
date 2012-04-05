package org.mammon.messaging;

/**
 * Transitionable objects that change their identity after a transition must
 * implement this interface to be reachable by their old identity in order to
 * meet the Redelivable requirement.
 */
public interface DualIdentityTransitionable extends Transitionable {

	/**
	 * @return the Transitionable object that accepts messages on the old
	 *         identity.
	 */
	Transitionable getSecondaryTransitionable();

}
