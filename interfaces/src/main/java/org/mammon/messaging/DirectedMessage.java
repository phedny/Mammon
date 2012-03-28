package org.mammon.messaging;

public interface DirectedMessage<I> extends Message {

	I getDestination();
	
}
