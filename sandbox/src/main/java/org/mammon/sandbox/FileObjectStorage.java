package org.mammon.sandbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

public class FileObjectStorage implements ObjectStorage {

	private static final Logger LOG = Logger.getLogger(FileObjectStorage.class.getName());

	private Map<String, Identifiable> runtimeObjectMap = new HashMap<String, Identifiable>();

	private Set<String> secondaryIdentities = new HashSet<String>();

	private final File root;

	private JsonUtil jsonUtil;

	public FileObjectStorage(JsonUtil jsonUtil, File root) {
		if (!root.isDirectory()) {
			throw new IllegalArgumentException("root must be a directory");
		}
		jsonUtil.setStorage(this);
		jsonUtil.registerClass(SecondaryTransitionable.class);
		this.jsonUtil = jsonUtil;
		this.root = root;
	}

	@Override
	public Identifiable get(String identity) {
		Identifiable object = runtimeObjectMap.get(identity);
		if (object == null) {
			FileInputStream fis = null;
			try {
				File file = getFile(identity);
				byte[] fileContents = new byte[(int) file.length()];
				fis = new FileInputStream(file);
				int p = 0;
				while (p < fileContents.length) {
					int r = fis.read(fileContents, p, fileContents.length - p);
					if (r < 0) {
						throw new AssertionError("Unable to read expected number of bytes");
					}
					p += r;
				}

				String json = new String(fileContents, "UTF-8");
				if (json != null) {
					Object deserializedObject = jsonUtil.deserializeObject(json);
					if (deserializedObject instanceof SecondaryTransitionable) {
						object = get((String) ((SecondaryTransitionable) deserializedObject).getPrimaryIdentity());
					} else {
						object = (Identifiable) deserializedObject;
					}
				}
			} catch (UnsupportedEncodingException e) {
				throw new AssertionError(e);
			} catch (IOException e) {
				LOG.log(Level.SEVERE, "Failed to read persisted object", e);
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					LOG.log(Level.SEVERE, "Failed to close input", e);
				}
			}
		}
		return object;
	}

	@Override
	public void remove(String identity) {
		Identifiable object = get(identity);
		if (object != null) {
			if (!identity.equals(object.getIdentity())) {
				throw new IllegalArgumentException("Argument must be primary identity");
			}

			LOG.fine("Removing " + identity);
			getFile(identity).delete();
			if (object instanceof DualIdentityTransitionable) {
				Transitionable secT = ((DualIdentityTransitionable) object).getSecondaryTransitionable();
				LOG.fine("Also removing " + secT.getIdentity());
				getFile(secT.getIdentity()).delete();
			}
		}
	}

	@Override
	public void replace(String identity, Identifiable object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(Identifiable object) {
		Class<?> clazz = jsonUtil.getRegisteredClass(object.getClass());
		if (clazz == null) {
			throw new IllegalArgumentException("No object interface has been registered for " + object.getClass());
		}

		File file = getFile(object.getIdentity());
		if (file.exists()) {
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

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
				writer.append(serializedObject).flush();
				fos.getFD().sync();
				writer.close();
			} catch (IOException e) {
				LOG.log(Level.SEVERE, "Failed to persist object", e);
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						LOG.log(Level.SEVERE, "Failed to close output", e);
					}
					fos = null;
				}
			}

			if (secT != null) {
				file = getFile(secT.getIdentity());
				try {
					fos = new FileOutputStream(file);
					OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
					writer
							.append(
									jsonUtil.serializeObject(new SecondaryTransitionable(object.getIdentity()
											.toString()), null)).flush();
					fos.getFD().sync();
					writer.close();
				} catch (IOException e) {
					LOG.log(Level.SEVERE, "Failed to persist secondary identity", e);
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							LOG.log(Level.SEVERE, "Failed to close output", e);
						}
					}
				}
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
			if (!runtimeObjectMap.containsKey(obj.getIdentity()) && !getFile(obj.getIdentity()).exists()) {
				store(obj);
			}
		}

		return serializedObject;
	}

	private File getFile(String identity) {
		try {
			return new File(root, URLEncoder.encode(identity, "UTF-8") + ".json");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public Iterator<Identifiable> iterator() {
		return new Iterator<Identifiable>() {

			private Iterator<Identifiable> iterator1 = runtimeObjectMap.values().iterator();

			private File[] files = root.listFiles();

			private Identifiable next = null;

			private int fileId = 0;

			@Override
			public boolean hasNext() {
				if (iterator1 != null) {
					if (iterator1.hasNext()) {
						return true;
					} else {
						iterator1 = null;
					}
				}
				try {
					do {
						String name = files[fileId++].getName();
						if (name.endsWith(".json")) {
							String objectId = name.substring(0, name.length() - 5);
							next = get(URLDecoder.decode(objectId, "UTF-8"));
							if (objectId.equals(next.getIdentity())) {
								return true;
							}
						}
					} while (fileId < files.length);
					return false;
				} catch (UnsupportedEncodingException e) {
					throw new AssertionError(e);
				}
			}

			@Override
			public Identifiable next() {
				if (iterator1 != null) {
					return iterator1.next();
				}
				if (fileId <= files.length) {
					return next;
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
