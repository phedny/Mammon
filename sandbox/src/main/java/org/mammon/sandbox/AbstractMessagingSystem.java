package org.mammon.sandbox;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.mammon.messaging.DirectedMessage;
import org.mammon.messaging.DualIdentityTransitionable;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transactable;
import org.mammon.messaging.Transitionable;

public abstract class AbstractMessagingSystem<I> {

	private ExampleObjectStorage storage = new ExampleObjectStorage();

	private Map<Class<?>, StateHandler<?>> stateHandlers = new HashMap<Class<?>, StateHandler<?>>();

	private ExecutorService executor = Executors.newSingleThreadExecutor();

	public AbstractMessagingSystem(Class<?>... registerClasses) {
		registerStateHandler(MessageEmitter.class, new MessageEmitterHandler());
		for (Class<?> registerClass : registerClasses) {
			storage.registerClass(registerClass);
		}
		for (Class<?> registerClass : getMessagingClasses()) {
			storage.registerClass(registerClass);
		}
	}

	public void shutdown() {
		executor.shutdown();
	}

	public void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		executor.awaitTermination(timeout, unit);
	}

	public void addObject(Identifiable object) {
		storage.store(object);
		enteredState(object, null);
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

	private void sendMessage(final Message message, final String destination, final String replyDestination) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				Identifiable destObj = storage.get(destination);
				System.out.println("Message " + message + " to " + destination + " (" + destObj + ")");
				Object newObject = null;

				if (destObj == null) {
					System.out.println("Discarded message");
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
								System.err.println(message + " transitioned " + destObj + " into " + newObject
										+ ", but fails Redeliverable requirement");
							}
						}
					}
					enteredState(newObject, replyDestination);
				}
			}
		});
	}

	protected abstract Class<?>[] getMessagingClasses();

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
			AbstractMessagingSystem.this.sendMessage(message, destination, identity);
		}

		@Override
		public void leftState(MessageEmitter object) {
			// Empty
		}

	}

}
