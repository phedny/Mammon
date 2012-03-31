package org.mammon.messaging;

/**
 * An object that is capable of emitting a message, must implement this
 * interface.
 */
public interface MessageEmitter {

	/**
	 * @return the message that this object emits.
	 */
	Message emitMessage();

}
