package org.mammon.sandbox;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.mammon.messaging.DirectedMessage;
import org.mammon.messaging.Identifiable;
import org.mammon.messaging.Message;
import org.mammon.messaging.MessageEmitter;
import org.mammon.messaging.Transactable;
import org.mammon.messaging.Transitionable;

public class MessagingSystem<I> {

	private Map<I, Identifiable<I>> objectMap = new HashMap<I, Identifiable<I>>();

	private Map<Class<?>, StateHandler<?, I>> stateHandlers = new HashMap<Class<?>, StateHandler<?, I>>();

	private ExecutorService executor = Executors.newSingleThreadExecutor();

	public MessagingSystem() {
		registerStateHandler(MessageEmitter.class, new MessageEmitterHandler());
	}

	public void shutdown() {
		executor.shutdown();
	}

	public void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		executor.awaitTermination(timeout, unit);
	}

	public void addObject(Identifiable<I> object) {
		objectMap.put(object.getIdentity(), object);
		enteredState(object, null);
	}

	public <C> void registerStateHandler(Class<C> clazz, StateHandler<C, I> stateHandler) {
		stateHandlers.put(clazz, stateHandler);
	}

	private <C> void enteredState(C object, I enteredBy) {
		Class<?> clazz = object.getClass();
		for (Entry<Class<?>, StateHandler<?, I>> entry : stateHandlers.entrySet()) {
			if (entry.getKey().isAssignableFrom(clazz)) {
				StateHandler<C, I> stateHandler = (StateHandler<C, I>) entry.getValue();
				stateHandler.enteredState(object, enteredBy);
			}
		}
	}

	private <C> void leftState(C object) {
		Class<?> clazz = object.getClass();
		for (Entry<Class<?>, StateHandler<?, I>> entry : stateHandlers.entrySet()) {
			if (entry.getKey().isAssignableFrom(clazz)) {
				StateHandler<C, I> stateHandler = (StateHandler<C, I>) entry.getValue();
				stateHandler.leftState(object);
			}
		}
	}

	public void sendMessage(DirectedMessage<I> message) {
		sendMessage(message, message.getDestination(), null);
	}

	private void sendMessage(final Message message, final I destination, final I replyDestination) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				Identifiable<I> destObj = objectMap.get(destination);
				System.out.println("Message " + message + " to " + destination + " (" + destObj + ")");
				Object newObject = null;

				if (destObj == null) {
					System.out.println("Discarded message");
				} else if (destObj instanceof Transactable) {
					Transactable t = (Transactable) destObj;
					Object reply = t.transact(message);
					if (reply != null) {
						if (reply instanceof DirectedMessage<?>) {
							sendMessage((Message) reply, ((DirectedMessage<I>) reply).getDestination(), destination);
						} else if (reply instanceof Message && replyDestination != null) {
							sendMessage((Message) reply, replyDestination, destination);
						} else {
							newObject = reply;
						}
					}
				} else if (destObj instanceof Transitionable) {
					Transitionable t = (Transitionable) destObj;
					newObject = t.transition(message);
					leftState(destObj);
					objectMap.remove(destination);
				}

				if (newObject != null) {
					if (newObject instanceof Identifiable<?>) {
						Identifiable<I> newIdentifiable = (Identifiable<I>) newObject;
						objectMap.put(newIdentifiable.getIdentity(), newIdentifiable);
						if (newObject instanceof Transitionable && destination.equals(newIdentifiable.getIdentity())) {
							Object testState = ((Transitionable) newObject).transition(message);
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

	private class MessageEmitterHandler implements StateHandler<MessageEmitter, I> {

		@Override
		public void enteredState(MessageEmitter object, I enteredBy) {
			Message message = object.emitMessage();
			I identity = null;
			if (object instanceof Identifiable<?>) {
				identity = (I) ((Identifiable<I>) object).getIdentity();
			}

			I destination = enteredBy;
			if (message instanceof DirectedMessage<?>) {
				destination = ((DirectedMessage<I>) message).getDestination();
			}
			MessagingSystem.this.sendMessage(message, destination, identity);
		}

		@Override
		public void leftState(MessageEmitter object) {
			// Empty
		}

	}

}
