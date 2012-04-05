package org.mammon.messaging;

/**
 * Any object that is identifiable, for example when it is to be used in the
 * messaging system, must implement this interface.
 */
public interface Identifiable {

	/**
	 * @return the identity of this object.
	 */
	String getIdentity();

}
