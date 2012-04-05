package org.mammon.sandbox;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mammon.messaging.AvailableAtRuntime;
import org.mammon.messaging.DualIdentityTransitionable;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.ObjectStorage;
import org.mammon.messaging.PersistAs;
import org.mammon.messaging.Transitionable;

public class ExampleObjectStorage<I> implements ObjectStorage<I> {

	private Map<Class, Constructor> storableInterfaces = new HashMap<Class, Constructor>();

	private Map<I, Identifiable<I>> objectMap = new HashMap<I, Identifiable<I>>();

	private Set<I> secondaryIdentities = new HashSet<I>();

	@Override
	public synchronized Identifiable<I> get(I identity) {
		return objectMap.get(identity);
	}

	@Override
	public void registerClass(Class<?> clazz) {
		AvailableAtRuntime availableAtRuntime = clazz.getAnnotation(AvailableAtRuntime.class);
		Class<?> iface = null;
		Constructor<?> constructor = null;

		if (availableAtRuntime == null) {
			for (Constructor<?> ctor : clazz.getConstructors()) {
				FromPersistent fromPersist = ctor.getAnnotation(FromPersistent.class);
				if (fromPersist != null) {
					iface = fromPersist.value();
					constructor = ctor;
				}
			}
			if (iface == null) {
				throw new IllegalArgumentException(clazz + " doesn't have @FromPersistent constructor");
			}

			int offset = 0;
			Class<?> enclosingClass = clazz.getEnclosingClass();
			if (enclosingClass != null) {
				if (getRegisteredClass(enclosingClass) == null) {
					throw new IllegalArgumentException("Enclosing class of " + clazz + " not registered");
				}
				offset = 1;
			}
			Class<?>[] types = constructor.getParameterTypes();
			Annotation[][] annotations = constructor.getParameterAnnotations();
			for (int i = offset; i < types.length; i++) {
				Class<?> type = types[i];
				PersistAs persistAs = null;
				for (Annotation annotation : annotations[i - offset]) {
					if (annotation instanceof PersistAs) {
						persistAs = (PersistAs) annotation;
					}
				}

				int arrayDepth = 0;
				while (type.isArray()) {
					type = type.getComponentType();
					arrayDepth++;
				}
				
				Class<?> registeredClass = null;
				if (persistAs == null) {
					throw new IllegalArgumentException("Argument " + i + " of " + clazz
							+ " constructor has no @PersistAt");
				} else if (String.class.isAssignableFrom(type)) {
					registeredClass = String.class;
				} else if (type.isPrimitive()) {
					registeredClass = type;
				} else {
					Class<?> componentType = type;
					registeredClass = getRegisteredClass(componentType);
					if (registeredClass == null && iface.isAssignableFrom(componentType)) {
						registeredClass = iface;
					}
					if (registeredClass == null) {
						throw new IllegalArgumentException("Argument " + i + " of " + clazz
								+ " constructor has incompatible type " + type);
					}

				}

				// Test for getter
				String getterName = "get" + persistAs.value().substring(0, 1).toUpperCase()
						+ persistAs.value().substring(1);
				try {
					Method getter = clazz.getMethod(getterName);
					Class<?> returnType = getter.getReturnType();
					int returnArrayDepth = 0;
					while (returnType.isArray()) {
						returnType = returnType.getComponentType();
						returnArrayDepth++;
					}
					if (!registeredClass.isAssignableFrom(returnType) && arrayDepth == returnArrayDepth) {
						throw new IllegalArgumentException(getterName + " method for argument " + i + " of " + clazz
								+ " constructor of wrong type");
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					throw new IllegalArgumentException("No " + getterName + " method found for argument " + i + " of "
							+ clazz + " constructor");
				}
			}

		} else {
			iface = availableAtRuntime.value();
		}

		if (storableInterfaces.containsKey(iface)) {
			// throw new IllegalArgumentException("Interface " + iface + " for "
			// + clazz + " already known");
		}

		storableInterfaces.put(iface, constructor);
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
		Class<?> clazz = getRegisteredClass(object.getClass());
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

	private Class<?> getRegisteredClass(Class<?> objectClass) {
		Class<?> clazz = null;
		for (Class<?> c : storableInterfaces.keySet()) {
			if (c.isAssignableFrom(objectClass)) {
				if (clazz == null) {
					clazz = c;
				} else if (c != clazz) {
					if (clazz.isAssignableFrom(c)) {
						clazz = c;
					} else if (!c.isAssignableFrom(clazz)) {
						throw new IllegalArgumentException("Object has ambiguous class registration for " + objectClass);
					}
				}
			}
		}
		return clazz;
	}

}
