package org.mammon.messaging;

/**
 * An implementation of this object is capable of persistently storing an
 * retrieving objects.
 */
public interface ObjectStorage extends Iterable<ObjectStorage.StoredObject> {

	/**
	 * Retrieve an object from persistent storage.
	 * 
	 * @param identity
	 *            The identity of the object to retrieve.
	 * @return The retrieved object.
	 */
	Identifiable get(String identity);

	/**
	 * Store an object into persistent storage.
	 * 
	 * @param object
	 *            The object to store.
	 * @param replyTo
	 *            Object to send message replies to.
	 */
	void store(Identifiable object, String replyTo);

	/**
	 * Replace an existing object in persistent storage.
	 * 
	 * @param identity
	 *            The identity of the existing object to replace.
	 * @param object
	 *            The replacement object.
	 * @param replyTo
	 *            Object to send message replies to.
	 */
	void replace(String identity, Identifiable object, String replyTo);

	/**
	 * Remove an existing object from persistent storage.
	 * 
	 * @param identity
	 *            The identity of the object to remove.
	 */
	void remove(String identity);

	/**
	 * This class is used when enumerating all stored objects.
	 */
	public class StoredObject {

		private final Identifiable object;

		private final String replyTo;

		public StoredObject(Identifiable object, String replyTo) {
			this.object = object;
			this.replyTo = replyTo;
		}

		/**
		 * Get the object.
		 * 
		 * @return the object.
		 */
		public Identifiable getObject() {
			return object;
		}

		/**
		 * Get the object to reply to.
		 * 
		 * @return the object to reply to.
		 */
		public String getReplyTo() {
			return replyTo;
		}

	}

}
