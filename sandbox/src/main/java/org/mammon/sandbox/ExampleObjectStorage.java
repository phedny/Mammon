package org.mammon.sandbox;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mammon.messaging.DualIdentityTransitionable;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.ObjectStorage;
import org.mammon.messaging.Transitionable;

public class ExampleObjectStorage implements ObjectStorage {
	
	private static final Logger LOG = Logger.getLogger(ExampleObjectStorage.class.getName());

	private Map<String, Identifiable> runtimeObjectMap = new HashMap<String, Identifiable>();

	private Map<String, String> persistedObjectMap = new HashMap<String, String>();

	private Set<String> secondaryIdentities = new HashSet<String>();
	
	private JsonUtil jsonUtil;

	public ExampleObjectStorage(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
		jsonUtil.setStorage(this);
		jsonUtil.registerClass(SecondaryTransitionable.class);
	}

	@Override
	public synchronized Identifiable get(String identity) {
		Identifiable object = runtimeObjectMap.get(identity);
		if (object == null) {
			String json = persistedObjectMap.get(identity);
			if (json != null) {
				object = (Identifiable) jsonUtil.deserializeObject(json);
			}
		}
		if (object instanceof SecondaryTransitionable) {
			object = get((String) ((SecondaryTransitionable) object).getPrimaryIdentity());
		}
		return object;
	}

	@Override
	public synchronized void remove(String identity) {
		if (secondaryIdentities.contains(identity)) {
			throw new IllegalArgumentException("Argument must be primary identity");
		}
		Identifiable object = runtimeObjectMap.get(identity);
		if (object != null) {
			LOG.fine("Removing " + identity);
			runtimeObjectMap.remove(object.getIdentity());
			if (object instanceof DualIdentityTransitionable) {
				Transitionable secT = ((DualIdentityTransitionable) object).getSecondaryTransitionable();
				LOG.fine("Also removing " + secT.getIdentity());
				runtimeObjectMap.remove(secT.getIdentity());
				secondaryIdentities.remove(secT.getIdentity());
			}
		}
	}

	@Override
	public synchronized void replace(String identity, Identifiable object) {
		remove(identity);
		store(object);
	}

	@Override
	public synchronized void store(Identifiable object) {
		Class<?> clazz = jsonUtil.getRegisteredClass(object.getClass());
		if (clazz == null) {
			throw new IllegalArgumentException("No object interface has been registered for " + object.getClass());
		}

		if (runtimeObjectMap.containsKey(object.getIdentity())) {
			throw new IllegalArgumentException("Object with this identity already exists, use replace()");
		}
		Transitionable secT = null;
		if (object instanceof DualIdentityTransitionable) {
			secT = ((DualIdentityTransitionable) object).getSecondaryTransitionable();
			if (runtimeObjectMap.containsKey(secT.getIdentity())) {
				throw new IllegalArgumentException(
						"Object with identity of secondary transitionable exists, use replace()");
			}
		}

		Class implementationClass = jsonUtil.getStorableInterface(clazz);
		Constructor implementationConstructor = jsonUtil.getImplementationConstructor(implementationClass.getName());
		if (implementationConstructor != null) {
			LOG.fine("Storing " + object.getIdentity() + " (" + object.getClass() + " as " + clazz + ")");
			String serializedObject = serializeObject(object);
			if (LOG.isLoggable(Level.FINER)) {
				LOG.finer(":-> " + serializedObject);
			}
			if (LOG.isLoggable(Level.FINEST)) {
				 Object deserializedObject = jsonUtil.deserializeObject(serializedObject);
				 LOG.finest(":<- " + deserializedObject);
				 LOG.finest(":.. " + object);
				 LOG.finest(":== " + deserializedObject.equals(object));
			}

			persistedObjectMap.put(object.getIdentity(), serializedObject);
			if (secT != null) {
				persistedObjectMap.put(secT.getIdentity(), jsonUtil.serializeObject(new SecondaryTransitionable(object
						.getIdentity().toString()), null));
				secondaryIdentities.add(secT.getIdentity());
			}
		} else {
			runtimeObjectMap.put(object.getIdentity(), object);
			if (secT != null) {
				runtimeObjectMap.put(secT.getIdentity(), secT);
				secondaryIdentities.add(secT.getIdentity());
			}
		}
	}

	public String serializeObject(Object object) {
		Set<Identifiable> referencedObjects = new HashSet<Identifiable>();
		String serializedObject = jsonUtil.serializeObject(object, referencedObjects);

		for (Identifiable obj : referencedObjects) {
			if (!runtimeObjectMap.containsKey(obj.getIdentity())
					&& !persistedObjectMap.containsKey(obj.getIdentity())) {
				store(obj);
			}
		}

		return serializedObject;
	}

	@Override
	public Iterator<Identifiable> iterator() {

		return new Iterator<Identifiable>() {

			private Iterator<String> iterator1 = persistedObjectMap.values().iterator();

			private Iterator<Identifiable> iterator2 = runtimeObjectMap.values().iterator();

			private Identifiable next = null;

			@Override
			public boolean hasNext() {
				if (iterator1 != null) {
					while (iterator1.hasNext()) {
						Object next = jsonUtil.deserializeObject(iterator1.next());
						if (!(next instanceof SecondaryTransitionable)) {
							this.next = (Identifiable) next;
							return true;
						}
					}
					iterator1 = null;
				}
				if (iterator2 != null) {
					if (iterator2.hasNext()) {
						return true;
					} else {
						iterator2 = null;
					}
				}
				return false;
			}

			@Override
			public Identifiable next() {
				if (iterator1 != null) {
					return (Identifiable) jsonUtil.deserializeObject(iterator1.next());
				}
				if (iterator2 != null) {
					return iterator2.next();
				}
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

}
