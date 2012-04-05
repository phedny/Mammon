package org.mammon.messaging;

/**
 * An object that is able to perform transactions initiated by messages must
 * implement this interface.
 */
public interface Transactable extends Identifiable {

	/**
	 * Initiate the transaction that is requested by the delivered message.
	 * 
	 * @param message
	 *            the message that initiates the transaction.
	 * @return either a DirectedMessage object, which will be sent to the
	 *         destination contained in the object, a Message object that is
	 *         returned to the sender, an Identifiable object that is added to
	 *         the messaging system or an object of type I. If the Identifiable
	 *         object is a MessageEmitter, the emitted message will be returned
	 *         to the sender. If an object of type I is returned, the message is
	 *         forwarded to the object with the given identity, such that a
	 *         reply is still returned to the original sender.
	 */
	Object transact(Message message);

}
