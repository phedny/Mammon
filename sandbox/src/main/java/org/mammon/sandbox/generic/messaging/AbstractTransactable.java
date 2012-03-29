package org.mammon.sandbox.generic.messaging;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mammon.messaging.Message;
import org.mammon.messaging.Transactable;

public abstract class AbstractTransactable implements Transactable {

	@Override
	public final Object transact(Message message) {
		Class<? extends Message> messageClass = message.getClass();
		try {
			Method method = this.getClass().getMethod("transact", messageClass);
			return method.invoke(this, message);
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
		}
		return null;
	}

}
