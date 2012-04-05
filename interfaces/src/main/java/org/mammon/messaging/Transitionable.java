package org.mammon.messaging;

/**
 * An object that represents the state of a state machine must implement this
 * interface, such that a transition into another state can be initiated by
 * delivery of a message.
 */
public interface Transitionable extends Identifiable {

	/**
	 * Initiate the state transition that is requested by the delivered message.
	 * 
	 * @param message
	 *            the message that initiates the state change.
	 * @return an object that represents the new state of the state machine or a
	 *         persistable Message object. If the new state object is a
	 *         MessageEmitter, the emitted message is returned to the sender and
	 *         the new state object must meet the Redeliverable requirement. The
	 *         Redeliverable requirement states that the identity of either one
	 *         of the following is true:
	 * 
	 *         - The identity of the new state object must equal the identity of
	 *         this state object, and invoking the transition() method on the
	 *         new state object, passing the message that initiated the state
	 *         transition into that state, must not initiate another state
	 *         transition and must return the current state object.
	 * 
	 *         - When the new state object does not implement Identifiable or
	 *         the identity of the new state object doesn't equal the identity
	 *         of the state object before the transition, the new state object
	 *         must implement DualIdentityTransitionable, such that invoking
	 *         getSecondaryTransitionable() on the new state object returns an
	 *         object that meets the previous statement.
	 * 
	 *         If a Message object is returned, the messaging system
	 *         automatically creates a virtual state, which does not allow any
	 *         new transitions, but does meet the Redeliverable requirement.
	 */
	Object transition(Message message);

}
