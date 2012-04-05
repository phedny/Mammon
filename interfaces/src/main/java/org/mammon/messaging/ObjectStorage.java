package org.mammon.messaging;

/**
 * An implementation of this object is capable of persistently storing an
 * retrieving objects.
 */
public interface ObjectStorage {

	/**
	 * Register a class, such that it can be persisted.
	 * 
	 * @param clazz
	 *            Class to use when instantiating an object of this interface.
	 */
	void registerClass(Class<?> clazz);

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
	 */
	void store(Identifiable object);

	/**
	 * Replace an existing object in persistent storage.
	 * 
	 * @param identity
	 *            The identity of the existing object to replace.
	 * @param object
	 *            The replacement object.
	 */
	void replace(String identity, Identifiable object);

	/**
	 * Remove an existing object from persistent storage.
	 * 
	 * @param identity
	 *            The identity of the object to remove.
	 */
	void remove(String identity);

}
