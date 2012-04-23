package org.mammon.sandbox;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
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
import org.mammon.messaging.FromPersistent;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.PersistAs;
import org.mammon.messaging.ReturnsEnclosing;

public class JsonUtil {

	private Map<Class, Class> storableInterfaces = new HashMap<Class, Class>();

	private Map<String, Constructor> implementationConstructors = new HashMap<String, Constructor>();

	private Map<Class, List<String>> classProperties = new HashMap<Class, List<String>>();

	private Map<Class, List<Class<?>>> classPropertyTypes = new HashMap<Class, List<Class<?>>>();
	
	private ExampleObjectStorage storage;

	public JsonUtil(Class<?>... registerClasses) {
		for (Class<?> registerClass : registerClasses) {
			registerClass(registerClass);
		}
		
	}
	
	public void setStorage(ExampleObjectStorage storage) {
		if (this.storage != null) {
			throw new IllegalStateException("Store has already been set");
		}
		this.storage = storage;
	}
	
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
				} else if (Number.class.isAssignableFrom(baseType)) {
					registeredClass = Number.class;
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
					if (!registeredClass.isAssignableFrom(returnType) || arrayDepth != returnArrayDepth) {
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

		storableInterfaces.put(iface, clazz);
		implementationConstructors.put(clazz.getName(), constructor);
		classProperties.put(clazz, properties);
		classPropertyTypes.put(clazz, propertyTypes);
	}

	public Object deserializeObject(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			return deserializeObjectJSON(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <C> C deserializeObject(String json, Class<C> implementation) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			jsonObject.put("implementation", implementation.getName());
			return (C) deserializeObjectJSON(jsonObject);
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
		} else if (BigInteger.class.isAssignableFrom(propertyType) && object instanceof Number) {
			return new BigInteger(object.toString());
		} else if (object instanceof String) {
			return storage.get((String) object);
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

	public String serializeObject(Object object, Set<Identifiable> referencedObjects) {
		if (referencedObjects == null) {
			referencedObjects = new HashSet<Identifiable>();
		}
		return serializeObjectJSON(object, referencedObjects, true).toString();
	}

	public String serializeKnownObject(Object object, Set<Identifiable> referencedObjects) {
		if (referencedObjects == null) {
			referencedObjects = new HashSet<Identifiable>();
		}
		return serializeObjectJSON(object, referencedObjects, false).toString();
	}

	private JSONObject serializeObjectJSON(Object object, Set<Identifiable> referencedObjects, boolean includeClassInfo) {
		JSONObject json = new JSONObject();
		Class<? extends Object> clazz = object.getClass();
		Class<?> registeredClass = getRegisteredClass(clazz);
		try {
			if (includeClassInfo) {
				json.put("interface", registeredClass.getName());
				json.put("implementation", clazz.getName());
			}
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
					} else if (value instanceof Identifiable) {
						Identifiable identifiable = (Identifiable) value;
						String identifier = identifiable.getIdentity().toString();
						json.put(property, identifier);
						referencedObjects.add(identifiable);
					} else {
						json.put(property, serializeObjectJSON(value, referencedObjects, true));
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

	private JSONArray serializeArrayJSON(Object value, Set<Identifiable> referencedObjects) {
		JSONArray array = new JSONArray();
		int length = Array.getLength(value);
		for (int i = 0; i < length; i++) {
			Object componentObject = Array.get(value, i);
			if (componentObject.getClass().isArray()) {
				array.put(serializeArrayJSON(componentObject, referencedObjects));
			} else {
				array.put(serializeObjectJSON(componentObject, referencedObjects, true));
			}
		}
		return array;
	}

	public Class<?> getRegisteredClass(Class<?> objectClass) {
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

	public Class getStorableInterface(Class<?> clazz) {
		return storableInterfaces.get(clazz);
	}

	public Constructor getImplementationConstructor(String name) {
		return implementationConstructors.get(name);
	}

}