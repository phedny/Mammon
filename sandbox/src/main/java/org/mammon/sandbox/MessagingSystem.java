package org.mammon.sandbox;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.mammon.messaging.DirectedMessage;
import org.mammon.messaging.DualIdentityTransitionable;
import org.mammon.messaging.ForwardRemote;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transactable;
import org.mammon.messaging.Transitionable;

public class MessagingSystem {

	private static final Logger LOG = Logger.getLogger(MessagingSystem.class.getName());

	private final JsonUtil jsonUtil;

	private final ExampleObjectStorage storage;

	private final Map<Class<?>, StateHandler<?>> stateHandlers = new HashMap<Class<?>, StateHandler<?>>();

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	private final RemoteMessaging remoteMessaging;

	public MessagingSystem(JsonUtil jsonUtil, String nodeId) throws IOException {
		this.jsonUtil = jsonUtil;
		jsonUtil.registerClass(StringRedeliverableMessage.class);
		jsonUtil.registerClass(NetworkMessage.class);
		jsonUtil.registerClass(PublishObject.class);
		storage = new ExampleObjectStorage(jsonUtil);
		registerStateHandler(MessageEmitter.class, new MessageEmitterHandler());
		remoteMessaging = new RemoteMessaging(this, jsonUtil, nodeId);
	}

	public void shutdown() {
		remoteMessaging.shutdown();
		executor.shutdown();
	}
	
	public void connect(String remoteHost) throws UnknownHostException, IOException {
		remoteMessaging.connect(remoteHost);
	}
	
	public String publish(String remoteId, Identifiable objectId) {
		return remoteMessaging.publish(remoteId, objectId);
	}

	public void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		executor.awaitTermination(timeout, unit);
	}

	public void addObject(Identifiable object) {
		addObject(object, false);
	}

	public void addObject(Identifiable object, boolean publishToAll) {
		storage.store(object);
		enteredState(object, null);
		if (publishToAll) {
			remoteMessaging.publish(null, object);
		}
	}

	public <C> void registerStateHandler(Class<C> clazz, StateHandler<C> stateHandler) {
		stateHandlers.put(clazz, stateHandler);
	}

	private <C> void enteredState(C object, String enteredBy) {
		Class<?> clazz = object.getClass();
		for (Entry<Class<?>, StateHandler<?>> entry : stateHandlers.entrySet()) {
			if (entry.getKey().isAssignableFrom(clazz)) {
				StateHandler<C> stateHandler = (StateHandler<C>) entry.getValue();
				stateHandler.enteredState(object, enteredBy);
			}
		}
	}

	private <C> void leftState(C object) {
		Class<?> clazz = object.getClass();
		for (Entry<Class<?>, StateHandler<?>> entry : stateHandlers.entrySet()) {
			if (entry.getKey().isAssignableFrom(clazz)) {
				StateHandler<C> stateHandler = (StateHandler<C>) entry.getValue();
				stateHandler.leftState(object);
			}
		}
	}

	public void sendMessage(DirectedMessage message) {
		sendMessage(message, message.getDestination(), null);
	}

	void sendMessage(Message message, final String destination, final String replyDestination) {
		final String serializedMessage = storage.serializeObject(message);

		executor.execute(new Runnable() {

			@Override
			public void run() {
				Message message = (Message) jsonUtil.deserializeObject(serializedMessage, null);
				Identifiable destObj = storage.get(destination);
				LOG.fine("Message " + serializedMessage + " to " + destination + " (" + destObj + ")");
				Object newObject = null;

				if (destObj == null || destObj.getClass().isAnnotationPresent(ForwardRemote.class)) {
					if (!remoteMessaging.sendMessage(message, destination, replyDestination)) {
						LOG.info("Discarded message: " + serializedMessage);
					}
				} else if (destObj instanceof Transactable) {
					Transactable t = (Transactable) destObj;
					Object reply = t.transact(message);
					if (reply != null) {
						if (reply instanceof DirectedMessage) {
							sendMessage((Message) reply, ((DirectedMessage) reply).getDestination(), destination);
						} else if (reply instanceof Message && replyDestination != null) {
							sendMessage((Message) reply, replyDestination, destination);
						} else if (reply instanceof String) {
							sendMessage(message, (String) reply, replyDestination);
						} else {
							newObject = reply;
						}
					}
				} else if (destObj instanceof Transitionable) {
					Transitionable t = (Transitionable) destObj;
					newObject = t.transition(message);
					leftState(destObj);
					storage.remove(destination);

					if (newObject instanceof Message) {
						newObject = new StringRedeliverableMessage(destination.toString(), message, (Message) newObject);
					}
				}

				if (newObject != null) {
					if (newObject instanceof Identifiable) {
						Identifiable newIdentifiable = (Identifiable) newObject;
						storage.store(newIdentifiable);
						Identifiable testObj = newIdentifiable;
						if (newObject instanceof DualIdentityTransitionable) {
							testObj = ((DualIdentityTransitionable) newObject);
						}
						if (newObject instanceof Transitionable && destination.equals(testObj.getIdentity())) {
							Object testState = ((Transitionable) testObj).transition(message);
							if (testState == null || !testState.equals(newObject)) {
								LOG.warning(message + " transitioned " + destObj + " into " + newObject
										+ ", but fails Redeliverable requirement");
							}
						}
					}
					enteredState(newObject, replyDestination);
				}
			}
		});
	}

	private class MessageEmitterHandler implements StateHandler<MessageEmitter> {

		@Override
		public void enteredState(MessageEmitter object, String enteredBy) {
			Message message = object.emitMessage();
			String identity = null;
			if (object instanceof Identifiable) {
				identity = ((Identifiable) object).getIdentity();
			}

			String destination = enteredBy;
			if (message instanceof DirectedMessage) {
				destination = ((DirectedMessage) message).getDestination();
			}
			MessagingSystem.this.sendMessage(message, destination, identity);
		}

		@Override
		public void leftState(MessageEmitter object) {
			// Empty
		}

	}

}
