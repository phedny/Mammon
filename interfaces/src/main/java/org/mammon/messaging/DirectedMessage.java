package org.mammon.messaging;

/**
 * Messages that are directed to a specific destination must implement this
 * interface.
 */
public interface DirectedMessage<I> extends Message {

	/**
	 * @return the identity of the destination of this message.
	 */
	I getDestination();

}
