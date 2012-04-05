package org.mammon.sandbox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mammon.messaging.DualIdentityTransitionable;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.ObjectStorage;
import org.mammon.messaging.Transitionable;

public class ExampleObjectStorage<I> implements ObjectStorage<I> {

	private Map<Class, Class> storableInterfaces = new HashMap<Class, Class>();

	private Map<I, Identifiable<I>> objectMap = new HashMap<I, Identifiable<I>>();

	private Set<I> secondaryIdentities = new HashSet<I>();

	@Override
	public synchronized Identifiable<I> get(I identity) {
		return objectMap.get(identity);
	}

	@Override
	public <C> void registerClass(Class<C> iface, Class<? extends C> clazz) {
		if (storableInterfaces.containsKey(iface)) {
			throw new IllegalArgumentException("Interface " + iface + " already known");
		}
		storableInterfaces.put(iface, clazz);
	}

	@Override
	public synchronized void remove(I identity) {
		if (secondaryIdentities.contains(identity)) {
			throw new IllegalArgumentException("Argument must be primary identity");
		}
		Identifiable<I> object = objectMap.get(identity);
		if (object != null) {
			objectMap.remove(object.getIdentity());
			if (object instanceof DualIdentityTransitionable<?>) {
				Transitionable<I> secT = ((DualIdentityTransitionable<I>) object).getSecondaryTransitionable();
				objectMap.remove(secT.getIdentity());
				secondaryIdentities.remove(secT.getIdentity());
			}
		}
	}

	@Override
	public synchronized void replace(I identity, Identifiable<I> object) {
		objectMap.remove(identity);
		objectMap.put(object.getIdentity(), object);
	}

	@Override
	public synchronized void store(Identifiable<I> object) {
		Class<?> clazz = null;
		for (Class<?> c : storableInterfaces.keySet()) {
			if (c.isAssignableFrom(object.getClass())) {
				if (clazz == null) {
					clazz = c;
				} else if (c != clazz) {
					if (clazz.isAssignableFrom(c)) {
						clazz = c;
					} else if (!c.isAssignableFrom(clazz)) {
						throw new IllegalArgumentException("Object has ambiguous class registration for "
								+ object.getClass());
					}
				}
			}
		}
		if (clazz == null) {
			throw new IllegalArgumentException("No object interface has been registered for " + object.getClass());
		}

		if (objectMap.containsKey(object.getIdentity())) {
			throw new IllegalArgumentException("Object with this identity already exists, use replace()");
		}
		Transitionable<I> secT = null;
		if (object instanceof DualIdentityTransitionable<?>) {
			secT = ((DualIdentityTransitionable<I>) object).getSecondaryTransitionable();
			if (objectMap.containsKey(secT.getIdentity())) {
				throw new IllegalArgumentException(
						"Object with identity of secondary transitionable exists, use replace()");
			}
		}

		System.out.println("Storing " + object.getIdentity() + " (" + object.getClass() + " as " + clazz + ")");
		objectMap.put(object.getIdentity(), object);
		if (secT != null) {
			objectMap.put(secT.getIdentity(), secT);
			secondaryIdentities.add(secT.getIdentity());
		}
	}

}
