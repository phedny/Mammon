package org.mammon.sandbox;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mammon.messaging.AvailableAtRuntime;
import org.mammon.messaging.DualIdentityTransitionable;
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.ObjectStorage;
import org.mammon.messaging.PersistAs;
import org.mammon.messaging.ReturnsEnclosing;
import org.mammon.messaging.Transitionable;

public class ExampleObjectStorage<I> implements ObjectStorage<I> {

	private Map<Class, Constructor> storableInterfaces = new HashMap<Class, Constructor>();

	private Map<String, Constructor> implementationConstructors = new HashMap<String, Constructor>();

	private Map<Class, List<String>> classProperties = new HashMap<Class, List<String>>();

	private Map<Class, List<Class<?>>> classPropertyTypes = new HashMap<Class, List<Class<?>>>();

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

		List<String> properties = new ArrayList<String>();
		List<Class<?>> propertyTypes = new ArrayList<Class<?>>();
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

				Method returnsEnclosing = null;
				for (Method method : clazz.getMethods()) {
					if (method.getAnnotation(ReturnsEnclosing.class) != null) {
						if (returnsEnclosing != null) {
							throw new IllegalArgumentException("More than one methods of " + clazz
									+ " have @ReturnsEnclosing");
						}
						returnsEnclosing = method;
					}
				}
				if (returnsEnclosing == null) {
					throw new IllegalArgumentException("No @ReturnsEnclosing method on " + clazz);
				}
				if (!returnsEnclosing.getReturnType().equals(enclosingClass)) {
					throw new IllegalArgumentException("@ReturnsEnclosing method on " + clazz
							+ " must have return type " + enclosingClass);
				}
				String methodName = returnsEnclosing.getName();
				char fourthChar = methodName.charAt(3);
				if (!methodName.startsWith("get") || fourthChar < 'A' || fourthChar > 'Z') {
					throw new IllegalArgumentException("@ReturnsEnclosing method on " + clazz
							+ " must have a proper getter name");
				}
				String propertyName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
				properties.add(propertyName);
				propertyTypes.add(enclosingClass);
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
				Class<?> baseType = type;
				while (baseType.isArray()) {
					baseType = baseType.getComponentType();
					arrayDepth++;
				}

				Class<?> registeredClass = null;
				if (persistAs == null) {
					throw new IllegalArgumentException("Argument " + i + " of " + clazz
							+ " constructor has no @PersistAt");
				} else if (String.class.isAssignableFrom(baseType)) {
					registeredClass = String.class;
				} else if (baseType.isPrimitive()) {
					registeredClass = baseType;
				} else {
					Class<?> componentType = baseType;
					registeredClass = getRegisteredClass(componentType);
					if (registeredClass == null && iface.isAssignableFrom(componentType)) {
						registeredClass = iface;
					}
					if (registeredClass == null) {
						throw new IllegalArgumentException("Argument " + i + " of " + clazz
								+ " constructor has incompatible type " + baseType);
					}

				}

				// Test for getter
				String propertyName = persistAs.value();
				String getterName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
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
				properties.add(propertyName);
				propertyTypes.add(type);
			}

		} else {
			iface = availableAtRuntime.value();
		}

		if (storableInterfaces.containsKey(iface)) {
			// throw new IllegalArgumentException("Interface " + iface + " for "
			// + clazz + " already known");
		}

		storableInterfaces.put(iface, constructor);
		implementationConstructors.put(clazz.getName(), constructor);
		classProperties.put(clazz, properties);
		classPropertyTypes.put(clazz, propertyTypes);
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

	private Object deserializeObject(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			return deserializeObjectJSON(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object deserializeObjectJSON(JSONObject json) throws JSONException {
		String implementation = json.getString("implementation");
		Constructor constructor = implementationConstructors.get(implementation);
		Class clazz = constructor.getDeclaringClass();
		List<String> properties = classProperties.get(clazz);
		List<Class<?>> propertyTypes = classPropertyTypes.get(clazz);

		Object[] arguments = new Object[properties.size()];
		for (int i = 0; i < arguments.length; i++) {
			String propertyName = properties.get(i);
			Class<?> propertyType = propertyTypes.get(i);
			arguments[i] = deserializePropertyJSON(json.get(propertyName), propertyType);
		}

		try {
			return constructor.newInstance(arguments);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Object deserializePropertyJSON(Object object, Class<?> propertyType) throws JSONException {
		if (propertyType.isArray() && object instanceof JSONArray) {
			return deserializeArrayJSON((JSONArray) object, propertyType.getComponentType());
		} else if (String.class.isAssignableFrom(propertyType) && object instanceof String) {
			return object;
		} else if (Integer.TYPE.isAssignableFrom(propertyType) && object instanceof Number) {
			return Integer.valueOf(((Number) object).intValue());
		} else if (Long.TYPE.isAssignableFrom(propertyType) && object instanceof Number) {
			return Long.valueOf(((Number) object).longValue());
		} else if (object instanceof String) {
			return get((I) object);
		} else if (object instanceof JSONObject) {
			return deserializeObjectJSON((JSONObject) object);
		} else {
			return null;

		}
	}

	private Object deserializeArrayJSON(JSONArray json, Class<?> expectedType) throws ArrayIndexOutOfBoundsException,
			IllegalArgumentException, JSONException {
		int length = json.length();
		Object array = Array.newInstance(expectedType, length);
		for (int i = 0; i < length; i++) {
			Array.set(array, i, deserializePropertyJSON(json.get(i), expectedType));
		}
		return array;
	}

	private String serializeObject(Object object, Set<Identifiable<I>> referencedObjects) {
		return serializeObjectJSON(object, referencedObjects).toString();
	}

	private JSONObject serializeObjectJSON(Object object, Set<Identifiable<I>> referencedObjects) {
		JSONObject json = new JSONObject();
		Class<? extends Object> clazz = object.getClass();
		Class<?> registeredClass = getRegisteredClass(clazz);
		try {
			json.put("interface", registeredClass.getName());
			json.put("implementation", clazz.getName());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		List<String> properties = classProperties.get(clazz);
		for (String property : properties) {
			String getterName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
			try {
				Method getter = clazz.getMethod(getterName);
				Object value = getter.invoke(object);

				if (value == null) {
					json.put(property, JSONObject.NULL);
				} else {
					Class<?> returnType = value.getClass();
					if (returnType.isArray()) {
						JSONArray array = serializeArrayJSON(value, referencedObjects);
						json.put(property, array);
					} else if (value instanceof String) {
						json.put(property, value);
					} else if (value instanceof Number) {
						json.put(property, value);
					} else if (value instanceof Identifiable<?>) {
						Identifiable<I> identifiable = (Identifiable<I>) value;
						String identifier = identifiable.getIdentity().toString();
						json.put(property, identifier);
						referencedObjects.add(identifiable);
					} else {
						json.put(property, serializeObjectJSON(value, referencedObjects));
					}
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	private JSONArray serializeArrayJSON(Object value, Set<Identifiable<I>> referencedObjects) {
		JSONArray array = new JSONArray();
		int length = Array.getLength(value);
		for (int i = 0; i < length; i++) {
			Object componentObject = Array.get(value, i);
			if (componentObject.getClass().isArray()) {
				array.put(serializeArrayJSON(componentObject, referencedObjects));
			} else {
				array.put(serializeObjectJSON(componentObject, referencedObjects));
			}
		}
		return array;
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
		Set<Identifiable<I>> referencedObjects = new HashSet<Identifiable<I>>();
		String serializedObject = serializeObject(object, referencedObjects);
		System.out.println(":-> " + serializedObject);

		for (Identifiable<I> obj : referencedObjects) {
			if (!objectMap.containsKey(obj.getIdentity())) {
				store(obj);
			}
		}

		try {
			Object deserializedObject = deserializeObject(serializedObject);
			System.out.println(":<- " + deserializedObject);
			System.out.println(":.. " + object);
			System.out.println(":== " + deserializedObject.equals(object));
		} catch (NullPointerException e) {
			System.out.println("!!! Failed to deserialize " + object);
		}
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
